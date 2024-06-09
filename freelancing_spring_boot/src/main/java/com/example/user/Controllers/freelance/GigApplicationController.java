package com.example.user.Controllers.freelance;

import com.example.user.Entities.freelance.Application;
import com.example.user.Entities.freelance.Gig;
import com.example.user.interfaces.freelance.IApplicationService;
import com.example.user.interfaces.freelance.IGigApplicationService;

import com.example.user.interfaces.freelance.IMilestoneGigApplicationService;
import com.example.user.repository.AppUserRepository;
import com.example.user.services.freelance.GigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Random;


/**
 * methods of this Controller:
 * getAll() allApplication belongs to a certainGig
 * getOne() a certain Application fetched by its id
 * post() adding an Application to a certain Gig
 * put() updating a certain Application fetched by its id
 * delete() deleting a certain Application  by its id
 */
@RestController
@RequestMapping("/gigs")
@CrossOrigin(origins = "*")
public class GigApplicationController {


    IGigApplicationService serviceGigApp;
    IMilestoneGigApplicationService serviceMilestoneGigApp;
    IApplicationService serviceApplication;
    private final AppUserRepository userRepo;
    private final GigService serviceGig;

    @Autowired
    public GigApplicationController(IGigApplicationService serviceGigApp, IApplicationService serviceApp, AppUserRepository userRepo, GigService serviceGig) {
        this.serviceGigApp = serviceGigApp;
        this.serviceApplication = serviceApp;
        this.userRepo = userRepo;
        this.serviceGig = serviceGig;
    }





    //-*************Methods****************
    @GetMapping("/{idGig}/applications")
    public List<Application> getAllGigIsApplications(@PathVariable Long idGig) {
        /*
            Verify user eligibility for the useCase by Spring security or manually
         */
        List<Application> apps =serviceGigApp.getAllApplicationFromGig(idGig);
        for (Application app : apps)
            this.serviceApplication.updateApplicationValuesBeforeReturn(app);

        return apps;
    }

    //-------------------------------------

    @PostMapping("/{idGig}/applications")
    public ResponseEntity<Gig> AddApplicationToGig(@PathVariable Long idGig, @RequestBody Application application) throws IOException {
         /*
            Verify user eligibility for the useCase by Spring security or manually
         */
        var loggedUserId = new Random().nextLong(4)+6;
        var connectedUser = this.userRepo.findById(loggedUserId).orElse(null);
        if(connectedUser == null)
            return ResponseEntity.status(204).build();

        //----check-role-if-necessary---In-business---------
            // not implemented
        application.setOwnerUser(connectedUser);
        Gig gig = serviceGigApp.addAppToGig(idGig, application);
        if (gig == null)
            return ResponseEntity.status(204).build();

        //get rid of extra irrelevant data of relationships from  gig and its applications
        this.serviceGig.gigViewDataUpdateBeforeReturn(gig);

        return ResponseEntity.status(201).body(gig);
    }


}