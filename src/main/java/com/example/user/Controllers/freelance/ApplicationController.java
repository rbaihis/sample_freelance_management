package com.example.user.Controllers.freelance;

import com.example.user.dto.freelance.AppUpdateDto;
import com.example.user.Entities.freelance.Application;
import com.example.user.interfaces.freelance.IApplicationService;
import com.example.user.repository.AppUserRepository;
import com.example.user.repository.freelance.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/gigs")
@CrossOrigin(origins = "*")
public class ApplicationController {

    private final AppUserRepository userRepo;
    private final IApplicationService serviceApplication;
    private final ApplicationRepository applicationRepo;

    @Autowired
    public ApplicationController(AppUserRepository userRepo, IApplicationService serviceApp, ApplicationRepository applicationRepo) {
        this.userRepo = userRepo;
        this.serviceApplication = serviceApp;
        this.applicationRepo = applicationRepo;
    }

    //-*************Methods-below*********************************************************


    @GetMapping("/applications/admin")
    public List<Application> getAllApplicationsByAdmin() {
        /*
            Admin only
         */
        return this.serviceApplication.getAllApplications();
    }
    //-------------------------------------


    @PutMapping("/applications/{id}")
    public ResponseEntity<Application> putByApplicationOwner(@PathVariable Long id, @RequestBody AppUpdateDto application) throws IOException {
         /*
            Only freelancer
         */
//        var loggedUserId = new Random().nextLong(4)+6;
//        var connectedUser = this.userRepo.findById(loggedUserId).orElse(null);
//        if(connectedUser == null)
//            return null;
//
//        //------not-the-owner-user
//        if( !application.getOwnerId().equals( connectedUser.getId()) )
//            return null;

        return serviceApplication.updateApplication(id, application) != null
                ? ResponseEntity.ok().build() : ResponseEntity.status(204).body(null);
    }

    //-------------------------------------
    @DeleteMapping("/applications/{id}")
    public ResponseEntity<String> deleteByApplicationOwner(@PathVariable Long id) throws IOException {

//        var loggedUserId = new Random().nextLong(4)+6;
//        var connectedUser = this.userRepo.findById(loggedUserId).orElse(null);
//        if(connectedUser == null)
//            ResponseEntity.badRequest().build();
//
        Application app = this.applicationRepo.findById(id).orElse(null);
        if(app == null)
            ResponseEntity.badRequest().build();
//
//        //------not-the-owner-user
//        if( !app.getOwnerId().equals( connectedUser.getId()) )
//            return ResponseEntity.badRequest().build();

        return this.serviceApplication.deleteApplication( app ) ?
                ResponseEntity.ok().build() : ResponseEntity.status(204).body(" you can't delete application on this stage");
    }
    //------------------------------

    @GetMapping("/applications/{id}")
    public Application getOne(@PathVariable Long id) {
        /*
            Verify user eligibility for the useCase by Spring security or manually
         */
        return serviceApplication.getApplication(id);
    }
    //-------------------------------------





}
