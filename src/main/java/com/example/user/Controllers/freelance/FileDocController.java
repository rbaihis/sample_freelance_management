package com.example.user.Controllers.freelance;


import com.example.user.services.freelance.FileDocDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("files/delete")
@CrossOrigin(origins = "*")
public class FileDocController {

    private final FileDocDbService fileService;
    @Autowired
    public FileDocController(FileDocDbService fileDbService){
        this.fileService = fileDbService;
    }


    @DeleteMapping("/{idFile}")
    public ResponseEntity<String> deleteFile(@PathVariable Long idFile , @RequestParam String entity) throws IOException {
        boolean isDeleted =this.fileService.deleteFileIfAllowed(idFile,entity);
        return isDeleted ? ResponseEntity.status(200).build() : ResponseEntity.status(204).build();
    }

}
