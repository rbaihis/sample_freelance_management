package com.example.user.Controllers.freelance;

import com.example.user.Entities.freelance.Milestone;
import com.example.user.interfaces.freelance.IMilestoneService;
import com.example.user.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/gigs")
@CrossOrigin(origins = "*")
public class MileStoneController {
    private final IMilestoneService serviceMilestone;
    private final AppUserRepository userRepo;
    @Autowired
    MileStoneController(IMilestoneService service, AppUserRepository userRepo){
        this.serviceMilestone =service;
        this.userRepo = userRepo;
    }

    //-********************************************************


    @GetMapping("/milestones/admin")
    public List<Milestone> getAllForAdmin() {
        /* admin only */
        return this.serviceMilestone.getMilestones();
    }

    @GetMapping("/milestones/my")
    public ResponseEntity<List<Milestone>> getMyMilestones() {

        var loggedUserId = new Random().nextLong(4);
        var connectedUser = this.userRepo.findById(loggedUserId).orElse(null);
        if(connectedUser == null)
            return ResponseEntity.status(206).body(null);

        List<Milestone> milestones= this.serviceMilestone.getMyMilestones(connectedUser.getId());
        System.out.println("milestones length : "+milestones.toArray().length);
        return  ResponseEntity.ok().body(milestones);
    }

    @GetMapping("/milestones/myAssigned")
    public ResponseEntity<List<Milestone>> getMyAssignedMilestones() {

        var loggedUserId = new Random().nextLong(4)+6;
        var connectedUser = this.userRepo.findById(loggedUserId).orElse(null);
        if(connectedUser == null)
            return ResponseEntity.status(206).body(null);

        List<Milestone> milestones= this.serviceMilestone.getMyAssignedMilestones(connectedUser.getId());
        milestones.forEach(ms-> System.out.println("ms-id: "+ms.getId()));
        System.out.println("milestones length : "+milestones.toArray().length);
        return  ResponseEntity.ok().body(milestones);
    }

    //----------------------
    @GetMapping("/milestones/{id}")
    public Milestone getOne(@PathVariable Long id){
        /* freelancer or GigCreator */
        return serviceMilestone.getOneMilestone( id);
    }


}
