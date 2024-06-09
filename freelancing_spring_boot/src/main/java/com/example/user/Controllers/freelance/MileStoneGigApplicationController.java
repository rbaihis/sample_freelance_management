package com.example.user.Controllers.freelance;

import com.example.user.dto.freelance.RefundResponse;
import com.example.user.Entities.freelance.Gig;
import com.example.user.interfaces.freelance.IMilestoneGigApplicationService;
import com.example.user.interfaces.freelance.IMilestoneService;
import com.example.user.repository.AppUserRepository;
import com.example.user.services.freelance.GigService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("gigs")
@CrossOrigin(origins = "*")
public class MileStoneGigApplicationController {

    private final IMilestoneGigApplicationService serviceMilestoneGigApp;
    private final IMilestoneService serviceMilestone;
    private final AppUserRepository userRepo;
    private final GigService gigService;

    public MileStoneGigApplicationController(IMilestoneGigApplicationService serviceMilestoneGigApp, IMilestoneService serviceMilestone, AppUserRepository userRepo, GigService gigService) {
        this.serviceMilestoneGigApp = serviceMilestoneGigApp;
        this.serviceMilestone = serviceMilestone;
        this.userRepo = userRepo;
        this.gigService= gigService;
    }

    //-********************************************************


    /**
     * This Will transform an application ==> applicationWonStatus or Cancel it
     * update in Gig, Milestone & ApplicationChosen will occur during this call if conditions re satisfied
     * @return type.Gig the gig that we re working on it
     */

    @GetMapping("/{idGig}/milestones/application/{idApp}/chosen")
    public ResponseEntity<Gig> choseApplicationWon(@PathVariable(name = "idGig")Long idGig, @PathVariable(name = "idApp")Long idApp){
        /* gig creator only*/

//        var loggedUserId = new Random().nextLong(4);
//        var connectedUser = this.userRepo.findById(loggedUserId).orElse(null);
//        if(connectedUser == null)
//            return ResponseEntity.status(205).body(null);
//
//
//        Gig gig = this.gigService.getOneGig(idGig);
//        if(gig==null || !gig.getOwnerUser().getId().equals(connectedUser.getId()))
//            return ResponseEntity.status(205).body(gig);
//
        Gig gig = this.gigService.getOneGig(idGig); // delete when u uncomment the above
        if (gig == null) return ResponseEntity.status(204).body(null); // delete when u uncomment above


        boolean success =  this.serviceMilestoneGigApp.ToggleAssignUnassignApplicationToGig(gig,idApp);
        System.out.println(" is operation successful : " + success);
        //update the extra data not saved in db

        //update gig data for view
        this.gigService.gigViewDataUpdateBeforeReturn(gig);

        if(!success) {
            return ResponseEntity.status(206).body(gig);
        }

        return ResponseEntity.status(200).body(gig);

    }

    //------------------------------
    //------------------------
    @GetMapping("/milestones/{id}/activate")
    public ResponseEntity<String> activateDeposit(@PathVariable Long id){
        /* GigCreator only  */

//        var loggedUserId = new Random().nextLong(4);
//        var connectedUser = this.userRepo.findById(loggedUserId).orElse(null);
//        if(connectedUser == null)
//            return ResponseEntity.status(204).body("no one is logged in the first place");
//
//
//        Milestone ms = this.serviceMilestone.getOneMilestone(id);
//        if(ms==null || !ms.getGig().getOwnerUser().getId().equals(connectedUser.getId()))
//            return ResponseEntity.status(204).body("not the owner Who Did the Action");


        return serviceMilestone.activateMileStone(id) ?
                ResponseEntity.ok().build() : ResponseEntity.status(206).body("action can't be done in this moment") ;

    }
    //----------------------------
    @GetMapping("/milestones/{id}/validate")
    public ResponseEntity<String>  validateTransfer(@PathVariable Long id){
        /*  GigCreator only */

//        var loggedUserId = new Random().nextLong(4);
//        var connectedUser = this.userRepo.findById(loggedUserId).orElse(null);
//        if(connectedUser == null)
//            return ResponseEntity.status(204).body("no one is logged in the first place");
//
//
//        Milestone ms = this.serviceMilestone.getOneMilestone(id);
//        if(ms==null || !ms.getGig().getOwnerUser().getId().equals(connectedUser.getId()))
//            return ResponseEntity.status(204).body("not the owner Who Did the Action");


        return serviceMilestone.validateMileStoneWork( id) ?
                ResponseEntity.ok().build() : ResponseEntity.status(206).body("some issue happen Can't do this action") ;
    }
    //-----------------------------
    @GetMapping("/milestones/{id}/report")
    public ResponseEntity<String>  report(@PathVariable Long id){
        /*  GigCreator only */

//        var loggedUserId = new Random().nextLong(4);
//        var connectedUser = this.userRepo.findById(loggedUserId).orElse(null);
//        if(connectedUser == null)
//            return ResponseEntity.status(204).body("no one is logged in the first place");
//
//
//        Milestone ms = this.serviceMilestone.getOneMilestone(id);
//        if(ms==null || !ms.getGig().getOwnerUser().getId().equals(connectedUser.getId()))
//            return ResponseEntity.status(204).body("not the owner Who Did the Action");


        return serviceMilestone.reportMileStone_customerNotSatisfied( id) ?
                ResponseEntity.ok().build() : ResponseEntity.status(206).build() ;
    }
    //--------------------------------
    @GetMapping("/milestones/{id}/refund")
    public ResponseEntity<RefundResponse> requestRefundOrPayment(@PathVariable(name = "id")Long idMilestone){
        /* gig creator or Freelancer */

//        var loggedUserId = new Random().nextLong(4);
//        var connectedUser = this.userRepo.findById(loggedUserId).orElse(null);
//        if(connectedUser == null)
//            return ResponseEntity.status(204).build();
//
//        Milestone ms = this.serviceMilestone.getOneMilestone(idMilestone);
//        if(ms==null )
//            return ResponseEntity.status(204).body(null);


        // this is the actual action important gigCreator or Freelancer Can do this Action
//        if( ms.getGig().getOwnerUser().getId().equals(connectedUser.getId()) ||  ms.getApplicationWon().getOwnerUser().getId().equals(connectedUser.getId()) )
            return ResponseEntity.ok().body(this.serviceMilestone.doRefundOrPayment(idMilestone ));


            //uncomment this on integration
//        return ResponseEntity.status(204).body(null);
    }







}
