package com.example.user.Controllers.freelance;

import com.example.user.dto.freelance.GigUpdateDto;
import com.example.user.Entities.freelance.Gig;
import com.example.user.interfaces.freelance.IGigMilestoneService;
import com.example.user.interfaces.freelance.IGigService;
import com.example.user.repository.AppUserRepository;
import com.example.user.repository.freelance.GigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/gigs")
@CrossOrigin(origins = "*")
public class GigController {

    IGigService serviceGig;
    IGigMilestoneService serviceGigMilestone;
    private final AppUserRepository userRepo;
    private final GigRepository gigrepo;
    @Autowired
    GigController(IGigService service, AppUserRepository userRepo,  GigRepository gigrepo){
        this.serviceGig =service;
        this.userRepo = userRepo;
        this.gigrepo = gigrepo;
    }


    //-*********************************************************************

    @GetMapping
    public List<Gig> getAllNotPaginated() {


        return serviceGig.getGigs();
    }

    @GetMapping("/paginated")
    public Page<Gig>  getGigsPaginated (@RequestParam("page") Integer page, @RequestParam(value = "sort",required = false) String sort , @RequestParam(value = "minPrice",required = false) Float minPrice, @RequestParam(value = "maxPrice",required = false) Float maxPrice) {


        //pageSize are fixed By Us
        int pageSize = 4;
        System.out.println("page: "+page+"      , sortOrder: "+(sort!=null ? sort : "nothing passed"));
        return this.serviceGig.getGigsPaginated(page,pageSize,sort,minPrice,maxPrice);
    }


    @GetMapping("/{id}")
    public Gig getOne(@PathVariable Long id){
        return serviceGig.getOneGig(id);
    }



    @PutMapping("/{id}")
    public ResponseEntity<Gig> put(@PathVariable Long id , @RequestBody GigUpdateDto gig)throws IOException{

//        var loggedUserId = new Random().nextLong(5);
//        var connectedUser = this.userRepo.findById(loggedUserId).orElse(null);
//        if(connectedUser == null)
//            ResponseEntity.status(204).build();
//
//        //------not-the-owner-user
//        if( !gig.getOwnerId().equals( connectedUser.getId()) )
//            return ResponseEntity.status(204).build();

        return  ResponseEntity.ok().body(serviceGig.updateGig(id , gig));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        //        var loggedUserId = new Random().nextLong(5);
//        var connectedUser = this.userRepo.findById(loggedUserId).orElse(null);
//        if(connectedUser == null)
//            ResponseEntity.status(204).build();

//      fetch Gig first and get the user   to use comparison

//        //------not-the-owner-user
//        if( !gig.getOwnerId().equals( connectedUser.getId()) )
//            return ResponseEntity.status(204).build();

        return serviceGig.deleteGig(id) ? ResponseEntity.accepted().build() : ResponseEntity.badRequest().build();

    }
}
