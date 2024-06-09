package com.example.user.services.freelance;

import com.example.user.dto.freelance.AppUpdateDto;
import com.example.user.Entities.freelance.Application;
import com.example.user.Entities.freelance.FileDoc;
import com.example.user.Entities.freelance.Gig;
import com.example.user.interfaces.freelance.IApplicationService;
import com.example.user.repository.freelance.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicationService  implements IApplicationService {

    private final ApplicationRepository repo;
    private final FileDocService serviceFileDoc;
    @Autowired
    ApplicationService(ApplicationRepository repo, FileDocService serviceFileDoc){
        this.repo = repo;
        this.serviceFileDoc = serviceFileDoc;
    }
    //--**************************---------------------*********************************
    @Override
    public void updateApplicationValuesBeforeReturn(Application app){
        //both replaced with JPQL would be better
        //----get only what u need
        app.setGigId(app.getGig().getId());
        //---get only what u need
        app.setOwnerId( app.getOwnerUser().getId());
        app.setOwnerName( app.getOwnerUser().getName());
        app.setCustomerId( app.getGig().getOwnerUser().getId());
    }
    //-----------------------------------

    public Application getApplication (Long id) {
        /*
        add logic to verify user's role can Read gigs && Application (notNeeded if Using SpringSecurity Options)
        if(loggedUser.role.equals(Role.GIG_VIEWER))
        */
        Application app = repo.findById(id).orElse(null);
        if (app == null)
            return null;

        this.updateApplicationValuesBeforeReturn(app);
        return app;
    }

    /**
     * for test purpose only
     * @return allApplications in DB
     */
    public List<Application> getAllApplications () {
        List<Application> apps= repo.findAll();
        for (Application app : apps)
            this.updateApplicationValuesBeforeReturn(app);

        return apps;
    }

    /**
     * this Will be Called By GiGApplicationService
     * !!! Can't be called Directly in Controller
     * @param application type.Application , must be filled With GigParent before Called.
     * @return type.Application after saved in Db.
     * @throws IOException if Issue happens when saving files
     */
    @Transactional
    public Application addApplicationNoMajorRelationReference_savesInDb(Application application) throws IOException {

        application.setApplicationWon(false);
        application.setSubmitDate(LocalDateTime.now());

        //order is important :
            // 1/generate-path-files (must always be first)
        if (application.getFiles() != null)
            for (FileDoc file : application.getFiles()) {
                serviceFileDoc.addUniqueFileName(file);
                serviceFileDoc.saveOnePhyFile(file);
            }

        Application app = repo.save(application);
        this.updateApplicationValuesBeforeReturn(app);
        return app;
    }

    @Transactional
    public Application updateApplication (Long idApp, AppUpdateDto appDto) throws IOException {

        Application applicationFetched = repo.findById(idApp).orElse(null);

        if(applicationFetched == null )
            return null;


        if (applicationFetched.getGig().getProjectStart().isBefore(LocalDate.now()))
            return null;

        if(applicationFetched.getGig().isGigAssigned())
            return null;
        //

        // !!! mandatory UniquePaths Should Be Generated To Dto before mapping
        if (appDto.getFiles() != null)
            for (FileDoc f : appDto.getFiles()) {
                this.serviceFileDoc.addUniqueFileName(f);
            }

        // add the updates to the original app
        AppUpdateDto.EntityMapperToDto(applicationFetched,appDto);
        //debug
        System.out.println(
                "***********:\n" +
                        " Dto-attributes :\n\t"
                        + (appDto.getFiles()!=null?appDto.getBid() :"null")
                        +"\n\t"+(appDto.getWalletId()!=null? appDto.getWalletId():"null")
                        +"\n\t"+(appDto.getBid()!=null?appDto.getBid() :"null")
                        +"\n\t"+(appDto.getExtraCost()!=null? appDto.getExtraCost():"null")
                        + "\n***************"
        );
        //end

        this.serviceFileDoc.savePhysFiles(applicationFetched.getFiles());
        Application app= repo.save(applicationFetched);
        this.updateApplicationValuesBeforeReturn(app);
        return  app;
    }

    /**
     * this method Can only be used By Parent Gig to toggle this Attribute .
     * !! can't be used in controller !!
     * Application is Saved in Db in this method
     * @param application type.Application ,application must be fully passed with its id and the Gig Related To It
     */
    @Override
    public void updateApplicationWonToggling_NoDbSaving(Application application ) {
        application.setApplicationWon( !application.isApplicationWon() );
    }


    @Transactional
    public boolean deleteApplication (Application applicationFetched) throws IOException {
        if(applicationFetched == null)
            return false;

        Gig gigFetched = applicationFetched.getGig();
        //Can't Delete Application if chosen By Customer already
        if (gigFetched != null && gigFetched.isGigAssigned() && applicationFetched.isApplicationWon())
            return false;

        return this.deletePhysicalFilesAndDeleteApplication(applicationFetched);
    }



    //---------private methods-------------

    /**
     * Deletes the Application as well its Related Static PhysicalFiles  .
     * @param app Application app , is the application u wish t delete .
     * @return true if application is deleted and no more exists in db ;
     * @throws IOException if IO related to deleting files occurred .
     */
    private boolean deletePhysicalFilesAndDeleteApplication(Application app) throws IOException {
        List<FileDoc> filesDeleted = serviceFileDoc.deletePhysFiles(app.getFiles());

        //debug-start if all file deleted
        System.out.println(  "**********\n" + (
                filesDeleted.isEmpty()? "AppRelatedFiles Successfully deleted " : "Files Not Deleted ") +
                "\n**********" );
        //debug-end

        Long appId = app.getId();
        repo.delete(app);
        return ! repo.existsById(appId);
    }



}
