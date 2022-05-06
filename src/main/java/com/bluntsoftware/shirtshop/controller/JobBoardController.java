package com.bluntsoftware.shirtshop.controller;

import com.bluntsoftware.shirtshop.model.JobBoard;
import com.bluntsoftware.shirtshop.service.JobBoardService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/job-board")
public class JobBoardController {

    private final JobBoardService service;

    public JobBoardController(JobBoardService service) {
        this.service = service;
    }

    @GetMapping(value = "",produces = MediaType.APPLICATION_JSON_VALUE)
    public JobBoard search(@RequestParam(value = "term",  defaultValue = "")String searchTerm){
        JobBoard jb = this.service.search(searchTerm);
        return jb;
    }

}
