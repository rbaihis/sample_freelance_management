package com.example.user.listeners;


import com.example.user.Entities.freelance.FileDoc;
import com.example.user.Entities.freelance.Gig;
import com.example.user.services.freelance.FileDocService;
import jakarta.persistence.PreRemove;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@NoArgsConstructor
@Setter
public class GigDeletionListener {
    private  FileDocService fileDocService ;
   @Autowired
    public GigDeletionListener(FileDocService fileDocService) {
       this.fileDocService = fileDocService;
    }


    @PreRemove
    public void handleGigDeletion(Object obj) throws IOException {
        Gig gig = (Gig) obj;
        gig.getFiles().forEach( (f)-> System.out.println(f.getPath()) );
        List<FileDoc> filesToDelete = new ArrayList<>(gig.getFiles()) ;
        gig.getApplications().forEach( app -> {
            filesToDelete.addAll(app.getFiles());
        } );
        gig.getMilestones().forEach( mileSt -> filesToDelete.addAll(mileSt.getMilestoneDeliverable()));

        this.fileDocService.deletePhysFiles(filesToDelete);

    }


}