package com.bluntsoftware.shirtshop.controller;

import com.bluntsoftware.shirtshop.model.FileItem;
import com.bluntsoftware.shirtshop.model.FileItems;
import com.bluntsoftware.shirtshop.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/rest/files")
public class FileController {

private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping({"/get/{id}"})
    public ResponseEntity<FileItem> get(@PathVariable(required = false) String id) throws IOException, GeneralSecurityException {
        FileItem file = fileService.getById(id);
        return ResponseEntity.ok(file);
    }

    @GetMapping({""})
    public ResponseEntity<List<FileItem>> listEverything(@RequestParam(required = false) String folderId) throws IOException, GeneralSecurityException {
        List<FileItem> files = folderId != null && !folderId.isEmpty() ?
                fileService.listAllFiles(folderId) : fileService.listEverything();
        return ResponseEntity.ok(files);
    }

    @GetMapping({"/list","/list/{parentId}"})
    public ResponseEntity<FileItems> list(@PathVariable(required = false) String parentId) throws IOException, GeneralSecurityException {
        return ResponseEntity.ok(fileService.listFolderContent(parentId));
    }

    @GetMapping("/download/{id}")
    public void download(@PathVariable String id, HttpServletResponse response) throws IOException, GeneralSecurityException {
        fileService.downloadFile(id, response.getOutputStream());
    }

    @GetMapping("/directory/create")
    public ResponseEntity<String> createDirectory(@RequestParam String path) throws Exception {
        String parentId = fileService.getFolderId(path);
        return ResponseEntity.ok("parentId: "+parentId);
    }

    @PostMapping(value = "/upload",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity< Map<String,String>> uploadSingleFile(@RequestBody MultipartFile file, @RequestParam(required = false) String path) {
        log.info("Request contains, File: " + file.getOriginalFilename());
        String fileId = fileService.uploadFile(file, path);
        if(fileId == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        Map<String,String> ret = new HashMap<>();
        ret.put("message","Success, FileId: "+ fileId);
        return ResponseEntity.ok(ret);
    }

    @GetMapping("/delete/{id}")
    public void delete(@PathVariable String id) throws Exception {
        fileService.deleteFile(id);
    }
}
