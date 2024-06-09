package com.example.user.Controllers.freelance;

import com.example.user.services.freelance.DisplayDownloadFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/files")


public class DisplayDownloadController {

    private final DisplayDownloadFilesService DisplayDownloadService ;
    @Autowired
    public DisplayDownloadController(DisplayDownloadFilesService displayDownloadService){
        DisplayDownloadService = displayDownloadService;
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> downloadFiles(@PathVariable String fileName) {

        Resource resource = this.DisplayDownloadService.downloadFile(fileName);

        if (resource == null)
            return ResponseEntity.badRequest().build();

        //removing the extra data generated for unique-naming
        int secondIndexOfUnderscore = resource.getFilename().indexOf('_', resource.getFilename().indexOf('_')+1)+1;
        String originalFileName = resource.getFilename().substring(secondIndexOfUnderscore);

        return ResponseEntity.ok()
                //this tells it will be downloaded at clientSide
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + originalFileName + "\"")
                .body(resource);
    }

    @GetMapping("/images/{fileName}")
    public ResponseEntity<byte[]> displayImage(@PathVariable String fileName) {

        byte [] imageInBytes;
        try {
           imageInBytes = this.DisplayDownloadService.displayImage(fileName);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageInBytes);
    }

}
