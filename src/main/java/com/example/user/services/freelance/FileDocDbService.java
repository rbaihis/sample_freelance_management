package com.example.user.services.freelance;

import com.example.user.Entities.freelance.Application;
import com.example.user.Entities.freelance.FileDoc;
import com.example.user.Entities.freelance.Gig;
import com.example.user.repository.freelance.ApplicationRepository;
import com.example.user.repository.freelance.FileDocRepo;
import com.example.user.repository.freelance.GigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;

@Service
public class FileDocDbService {
    private final GigRepository gigRepo;
    private final ApplicationRepository appRepo;
    private final FileDocRepo fileRepo;
    private final FileDocService physFileService;
    @Autowired
    public FileDocDbService(GigRepository gigRepo, ApplicationRepository appRepo, FileDocRepo fileRepo, FileDocService physFileService) {
        this.gigRepo = gigRepo;
        this.appRepo = appRepo;
        this.fileRepo = fileRepo;
        this.physFileService = physFileService;
    }


    @Transactional
    public boolean deleteFileIfAllowed( Long fileId , String entityName) throws IOException {

        String entity = entityName.toLowerCase();
        switch ( entity){
            case "gig" :
                return this.DeleteGigIsFile(fileId);
            case "application":
                return this.DeleteApplicationIsFile(fileId);
        }

        return false;
    }


    private  boolean  DeleteGigIsFile(Long fileId)throws IOException{
        Gig gig = this.gigRepo.findGigByFilesId(fileId).orElse(null);
        if (gig == null)
            return false;

        if ( gig.isGigAssigned() || gig.getProjectStart().isBefore(LocalDate.now()) )
            return false;

        FileDoc fileDoc = gig.getFiles().stream().filter(f -> f.getId().equals(fileId)).findFirst().orElse(null);
        if (fileDoc == null)
            return false;

        gig.getFiles().remove(fileDoc);
        System.out.println("file to delete "+ fileDoc.getName());
        System.out.println("path to delete"+fileDoc.getPath());
        physFileService.deleteOnePhysFile(fileDoc);
        this.gigRepo.save(gig);
        return true;
    }



    private  boolean  DeleteApplicationIsFile(Long fileId)throws IOException{
        Application app = this.appRepo.findApplicationByFilesId(fileId).orElse(null);
        if ( app == null)
            return false;

        if ( app.getGig().isGigAssigned() || app.getGig().getProjectStart().isBefore(LocalDate.now()) )
            return false;

        FileDoc fileDoc = app.getFiles().stream().filter(f -> f.getId().equals(fileId)).findFirst().orElse(null);
        if (fileDoc == null)
            return false;

        app.getFiles().remove(fileDoc);
        System.out.println("file to delete "+ fileDoc.getName());
        System.out.println("path to delete"+fileDoc.getPath());
        physFileService.deleteOnePhysFile(fileDoc);
        this.appRepo.save(app);

        return true;
    }

}
