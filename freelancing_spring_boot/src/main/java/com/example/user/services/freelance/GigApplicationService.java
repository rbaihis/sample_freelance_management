package com.example.user.services.freelance;

import com.example.user.Entities.freelance.Application;
import com.example.user.Entities.freelance.Gig;
import com.example.user.interfaces.freelance.IApplicationService;
import com.example.user.interfaces.freelance.IGigApplicationService;
import com.example.user.interfaces.freelance.IGigService;
import com.example.user.repository.freelance.ApplicationRepository;
import com.example.user.repository.freelance.GigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class GigApplicationService implements IGigApplicationService {

    IApplicationService serviceApplication;
    IGigService serviceGig;
    GigRepository gigRepo;
    ApplicationRepository applicationRepository;

    @Autowired
    public GigApplicationService(IApplicationService serviceApplication, IGigService serviceGig ,
                                 GigRepository gigRepo ,     ApplicationRepository applicationRepository
    ) {
        this.serviceApplication = serviceApplication;
        this.serviceGig = serviceGig;
        this.gigRepo =gigRepo;
        this.applicationRepository = applicationRepository;
    }
    //----------------------------------------------

    @Override
    public List<Application> getAllApplicationFromGig(Long idGig) {
        Gig gigFetched = serviceGig.getOneGig(idGig);
        if(gigFetched==null)
            return null;

        List<Application> apps = gigFetched.getApplications();

        for (Application app : apps )
            this.serviceApplication.updateApplicationValuesBeforeReturn(app);

        return apps;
    }



    @Override
    @Transactional
    public Gig addAppToGig(Long idGig , Application app) throws IOException {
        Gig gigFetched = serviceGig.getOneGig(idGig);

        if (gigFetched == null )
            return null;
        if (gigFetched.getProjectStart().isBefore(LocalDate.now()))
            return null;
        //if an application already win the gig there is no need for freelancers to add application anymore
        if (gigFetched.isGigAssigned())
            return null;

        app.setGig(gigFetched);
        gigFetched.getApplications().add(
                //method_called Does not add the app.setGig(gigFetched);
                this.serviceApplication.addApplicationNoMajorRelationReference_savesInDb(app)
        );

        this.serviceGig.gigViewDataUpdateBeforeReturn(gigFetched);
        return gigFetched;
    }









}
