package com.example.user.Controllers.freelance;

import com.example.user.Entities.freelance.Gig;
import com.example.user.Entities.freelance.Milestone;
import com.example.user.interfaces.freelance.IGigMilestoneService;
import com.example.user.interfaces.freelance.IGigService;
import com.example.user.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/gigs")
@CrossOrigin(origins = "*")
public class GigMilestoneController {


    private final AppUserRepository userRepo;
    private final IGigMilestoneService serviceGigMilestone ;
    private final IGigService serviceGig ;
    @Autowired
    GigMilestoneController( AppUserRepository userRepo, IGigMilestoneService serviceGigMilestone, IGigService serviceGig){
        this.userRepo = userRepo;
        this.serviceGigMilestone = serviceGigMilestone;
        this.serviceGig = serviceGig;
    }
    //-******************************************************




    @GetMapping("/{idGig}/milestones")
    public List<Milestone> getGigIsgMilestones(@PathVariable Long idGig) {

        return serviceGig.getOneGig(idGig).getMilestones();
    }
    //--------------------------
    /**
     * mandatory to add gig with its milestones , can't add milestones separately
     */
    @PostMapping
    public Gig addGigMandatoryWithMilestones_MilestonesAreMandatory(@RequestBody Gig gig)throws IOException {
         var loggedUserId = new Random().nextLong(5);
         var connectedUser = this.userRepo.findById(loggedUserId).orElse(null);
         if(connectedUser == null)
             return null;

         gig.setOwnerUser(connectedUser);
        return this.serviceGigMilestone.addGig(gig);
    }


    // no use case for this , i decided this is not Updatable/Deletable.
//    @DeleteMapping("/{idGig}/milestones/{id}")
//    public ResponseEntity<String> delete(@PathVariable Long idGig,@PathVariable Long id){
//
//        return service.deleteMilestone(id) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build() ;
//    }

}
