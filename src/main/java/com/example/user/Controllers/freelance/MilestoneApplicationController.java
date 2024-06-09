package com.example.user.Controllers.freelance;

import com.example.user.dto.freelance.MileStoneUpdateDto;
import com.example.user.Entities.freelance.Milestone;
import com.example.user.interfaces.freelance.IMilestoneService;
import com.example.user.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("gigs/milestones/{id}/application")
@CrossOrigin(origins = "*")
public class MilestoneApplicationController {

    private final IMilestoneService serviceMilestone;
    private final AppUserRepository userRepo;

    @Autowired
    MilestoneApplicationController(IMilestoneService service, AppUserRepository userRepo){
        this.serviceMilestone =service;
        this.userRepo = userRepo;
    }
    //-*******************************************************************

    @PutMapping("/files")
    public ResponseEntity<Milestone> UpdateMilestoneByOnlyAddingFiles(@PathVariable Long id, @RequestBody MileStoneUpdateDto milestone)
            throws IOException {

//        var loggedUserId = new Random().nextLong(5);
//        var connectedUser = this.userRepo.findById(loggedUserId).orElse(null);
//        if(connectedUser == null)
//            return ResponseEntity.status(204).body(null);
//
//        //change this by accessing the relationship all the way ms->appWon->owner->ownerId
//        if( ! milestone.getFreelancerId().equals(connectedUser.getId()))
//            return ResponseEntity.status(204).body(null);

        Milestone ms = this.serviceMilestone.updateAddFilesToMileStone(id,milestone);

        int statusCode =  (ms != null ? 200 : 204);

        return  ResponseEntity.status(statusCode).body(ms);
    }

    //----------------------------------------------

    @GetMapping("/validate")
    @Transactional
    public ResponseEntity<String> UpdateMilestoneByValidatingMyFreelanceMilestoneWorkIsDone(@PathVariable("id") Long id)
            throws IOException {
        /* freelancer only */

//        var loggedUserId = new Random().nextLong(5);
//        var connectedUser = this.userRepo.findById(loggedUserId).orElse(null);
//        if(connectedUser == null)
//            return ResponseEntity.status(204).body(null);
//
//        Milestone ms = this.serviceMilestone.getOneMilestone(id);
//
//        //change this by accessing the relationship all the way ms->appWon->owner->ownerId
//        if( ! ms.getFreelancerId().equals(connectedUser.getId()))
//            return ResponseEntity.status(204).body(null);



        return serviceMilestone.validateWorkDoneByApplicationWon(id) ?
                ResponseEntity.ok().build():ResponseEntity.badRequest().build();
    }


}
