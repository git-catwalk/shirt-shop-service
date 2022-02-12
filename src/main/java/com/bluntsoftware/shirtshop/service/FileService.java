package com.bluntsoftware.shirtshop.service;

import com.bluntsoftware.shirtshop.integrations.google.service.GoogleDriveManager;
import com.bluntsoftware.shirtshop.model.FileItem;
import com.bluntsoftware.shirtshop.model.FileItems;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FileService {

    private final GoogleDriveManager googleDriveManager;

    public FileService(GoogleDriveManager googleDriveManager) {
        this.googleDriveManager = googleDriveManager;
    }

    public List<FileItem> listEverything() throws IOException, GeneralSecurityException {
        // Print the names and IDs for up to 10 files.
        FileList result = googleDriveManager.getInstance().files().list()
                .setPageSize(100)
                .setFields("nextPageToken, files(mimeType,id, name,kind,createdTime,parents,modifiedDate,description,size,headRevisionId)")
                .execute();

        return result.getFiles().stream()
                .map(this::convert).
                collect(Collectors.toList());
    }

    public List<FileItem> listAllFiles(String folderId) throws GeneralSecurityException, IOException {
        List<FileItem> files = new ArrayList<>();
        listFilesAndSubfolders(folderId,files);
        return files;
    }

    private void listFilesAndSubfolders(String folderId,List<FileItem> list) throws GeneralSecurityException, IOException {
        FileItems fileItems = this.listFolderContent(folderId);
        list.addAll(fileItems.getFiles());
        for (FileItem f : fileItems.getFolders()) {
            listFilesAndSubfolders(f.getId(), list);
        }
    }
    public FileItems listFolderContent(String parentId) throws IOException, GeneralSecurityException {
        if (parentId == null) {
            parentId = "root";
        }
        String query = "'" + parentId + "' in parents";
        FileList result = googleDriveManager.getInstance().files().list()
                .setQ(query)
                .setPageSize(100)
                .setFields("nextPageToken, files(parents,modifiedTime,mimeType,id, name,kind,createdTime,description,size,headRevisionId)")
                .execute();

        FileItems fileItems = FileItems.builder()
                .files(new ArrayList<>())
                .folders(new ArrayList<>())
                .path(new ArrayList<>())
                .build();

        result.getFiles().forEach((f)->{
             if(f.getMimeType() != null && f.getMimeType().endsWith("folder")){
                 fileItems.getFolders().add(convert(f));
             }else{
                 fileItems.getFiles().add(convert(f));
             }
        });
        fileItems.setPath(buildPath(parentId));
        return fileItems;
    }

    private List<Map<String, String>> buildPath(String parentId) throws GeneralSecurityException, IOException {
        List<Map<String, String>> ret = new ArrayList<>();
        getPath(parentId,ret);
        Collections.reverse(ret);
        return ret;
    }
    private void getPath(String parentId,List<Map<String, String>> path) throws GeneralSecurityException, IOException {
        FileItem fileItem = getById(parentId);
        if(fileItem != null){
            Map<String,String> pathLink = new HashMap<>();
            pathLink.put("id",fileItem.getId() );
            pathLink.put("name",fileItem.getName() );
            path.add(pathLink);
            if(fileItem.getFolderId() != null){
              getPath(fileItem.getFolderId(),path);
            }
        }
    }

    public FileItem getById(String id) throws GeneralSecurityException, IOException {
        File file = googleDriveManager.getInstance().files()
                .get(id)
                .setFields("parents,modifiedTime,mimeType,id,name,kind,createdTime,description,size")
                .execute();
        if(file != null){
            return convert(file);
        }
       return null;
    }

    private FileItem convert(File f){
        List<String> parents = f.getParents();
        String folderId = null;
        if(parents != null && parents.size() > 0){
            folderId = parents.get(0);
        }

        return FileItem.builder()
                .createdAt(f.getCreatedTime() != null ? f.getCreatedTime().toString() : "")
                .modifiedAt(f.getModifiedTime() != null ? f.getModifiedTime().toString() : "")
                .description(f.getDescription())
                .id(f.getId())
                .folderId(folderId)
                .name(f.getName())
                .size(f.getSize() != null ? f.getSize().toString() : "0")
                .type(f.getMimeType())
                .build();
    }

    public void downloadFile(String id, OutputStream outputStream) throws IOException, GeneralSecurityException {
        if (id != null) {
            String fileId = id;
            googleDriveManager.getInstance().files().get(fileId).executeMediaAndDownloadTo(outputStream);
        }
    }

    public void deleteFile(String fileId) throws Exception {
        googleDriveManager.getInstance().files().delete(fileId).execute();
    }

    public String uploadFile(MultipartFile file, String filePath) {
        try {
            String folderId = getFolderId(filePath);
            if (null != file) {
                File fileMetadata = new File();
                fileMetadata.setParents(Collections.singletonList(folderId));
                fileMetadata.setName(file.getOriginalFilename());
                File uploadFile = googleDriveManager.getInstance()
                        .files()
                        .create(fileMetadata, new InputStreamContent(
                                file.getContentType(),
                                new ByteArrayInputStream(file.getBytes()))
                        )
                        .setFields("id").execute();
                return uploadFile.getId();
            }
        } catch (Exception e) {
            log.error("Error: ", e);
        }
        return null;
    }

    public String getFolderId(String path) throws Exception {
        String parentId = null;
        String[] folderNames = path.split("/");

        Drive driveInstance = googleDriveManager.getInstance();
        for (String name : folderNames) {
            parentId = findOrCreateFolder(parentId, name, driveInstance);
        }
        return parentId;
    }

    private String findOrCreateFolder(String parentId, String folderName, Drive driveInstance) throws Exception {
        String folderId = searchFolderId(parentId, folderName, driveInstance);
        // Folder already exists, so return id
        if (folderId != null) {
            return folderId;
        }
        //Folder dont exists, create it and return folderId
        File fileMetadata = new File();
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        fileMetadata.setName(folderName);

        if (parentId != null) {
            fileMetadata.setParents(Collections.singletonList(parentId));
        }
        return driveInstance.files().create(fileMetadata)
                .setFields("id")
                .execute()
                .getId();
    }

    private String searchFolderId(String parentId, String folderName, Drive service) throws Exception {
        String folderId = null;
        String pageToken = null;
        FileList result = null;

        File fileMetadata = new File();
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        fileMetadata.setName(folderName);

        do {
            String query = " mimeType = 'application/vnd.google-apps.folder' ";
            if (parentId == null) {
                query = query + " and 'root' in parents";
            } else {
                query = query + " and '" + parentId + "' in parents";
            }
            result = service.files().list().setQ(query)
                    .setSpaces("drive")
                    .setFields("nextPageToken, files(id, name)")
                    .setPageToken(pageToken)
                    .execute();

            for (File file : result.getFiles()) {
                if (file.getName().equalsIgnoreCase(folderName)) {
                    folderId = file.getId();
                }
            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null && folderId == null);

        return folderId;
    }


}
