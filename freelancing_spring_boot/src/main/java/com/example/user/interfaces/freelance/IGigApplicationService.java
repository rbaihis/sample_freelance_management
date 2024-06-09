package com.example.user.interfaces.freelance;

import com.example.user.Entities.freelance.Application;
import com.example.user.Entities.freelance.Gig;

import java.io.IOException;
import java.util.List;


public interface IGigApplicationService {


    //----------------------------------------------

    public List<Application> getAllApplicationFromGig(Long idGig);



    public Gig addAppToGig(Long idGig , Application app) throws IOException ;





}
