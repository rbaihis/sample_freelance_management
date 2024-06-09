package com.example.user.services.freelance;

import com.example.user.Entities.freelance.Application;
import com.example.user.Entities.freelance.Gig;
import com.example.user.Entities.freelance.Milestone;
import com.example.user.interfaces.freelance.IApplicationService;
import com.example.user.interfaces.freelance.IGigService;
import com.example.user.interfaces.freelance.IMilestoneGigApplicationService;
import com.example.user.services.freelance.utils.email.HtmlTemplateMange;
import com.example.user.services.freelance.utils.email.IEmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class MilestoneGigApplicationService implements IMilestoneGigApplicationService {


    private final IGigService serviceGig;
    private final IApplicationService serviceApplication;
    private final IEmailSender emailSenderService;
    private final HtmlTemplateMange htmlTemplateManger;

    @Autowired
    public MilestoneGigApplicationService(GigService serviceGig, ApplicationService serviceApplication, IEmailSender emailSenderService, HtmlTemplateMange htmlTemplateManger) {
        this.serviceGig = serviceGig;
        this.serviceApplication = serviceApplication;
        this.emailSenderService = emailSenderService;
        this.htmlTemplateManger = htmlTemplateManger;
    }

    //*****************************************************


    @Override
    public void assignApplicationWonToGigIsMileStones (Gig gig , Application applicationWon){
        for (Milestone ms : gig.getMilestones())
            ms.setApplicationWon(applicationWon);
    }
    //--------------------------------------------
    @Override
    public void unAssignApplicationWonFromGigIsMileStones(Gig gig ) {

        for (Milestone ms : gig.getMilestones())
            ms.setApplicationWon(null);
    }



    //-------------------------------------------------
    @Override
    @Transactional
    public boolean ToggleAssignUnassignApplicationToGig( Gig gigFetched , Long id){
        if (gigFetched == null)
            return false;

        //update can not happen if delay expired create new gig instead
        //why? freeLancer may not be able to meet deadline, or he can not Commit to project if starts late , better avoid completely
        if(gigFetched.getProjectStart().isBefore(LocalDate.now()))
            return false;

        //already had milestone Deposited freelancer already working it (customer may accept an application before Deadline but if deposit occurred means agreed that person will be working on it)
        if(gigFetched.getMilestones().stream().anyMatch(Milestone::isDeposited))
            return false;

        //------
        //get Application From relationship since its already there
        Application appChosen = gigFetched.getApplications()
                .stream()
                .filter( app -> app.getId().equals(id) ).findFirst().orElse(null);
        System.out.println("application to assign : " + appChosen );

        if (appChosen == null )
            return false;
        //---------

        if( !appChosen.isApplicationWon() && gigFetched.isGigAssigned() ){
            System.out.println("******Bug-illegalAction-customer-try-to-enable-2-different app for the same gig*******");
            System.out.println("gig is assigned: "+gigFetched.isGigAssigned());
            System.out.println("application isApplicationWon: "+appChosen.isApplicationWon());
            System.out.println("*************");
            return false;
        }

        boolean isToggleOn =!appChosen.isApplicationWon() ;


        this.serviceApplication.updateApplicationWonToggling_NoDbSaving(appChosen);
        gigFetched.setGigAssigned( !gigFetched.isGigAssigned() );

        //vars template {{ action }}{{ name }}{{ date }}
        String body = this.htmlTemplateManger.getHtmlBodyAsString("TestTemplate")
                .replace("{{ name }}",appChosen.getOwnerUser().getName())
                .replace("{{ date }}", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE));;


        if(isToggleOn){
            this.assignApplicationWonToGigIsMileStones(gigFetched,appChosen); //sets gigMilestones[i].applicationWon = appChosen

            //sending Email to freelancer chosen
            String finalBody= body.replace("{{ action }}", "You have been chosen as freelancer to work on his Gig.\n Gig Title : "+gigFetched.getTitle()+" .");
            this.emailSenderService.sendEmailWithAttachmentHtmlBodyOrNoOk(appChosen.getOwnerUser().getEmail(),"You Have Been Chosen as Freelancer", finalBody,"noReply@ghostech.com",null,true);
        }
        else {
            this.unAssignApplicationWonFromGigIsMileStones(gigFetched); //sets gigMilestones[i].applicationWon = null
            //sending Email to freelancer chosen
            String finalBody= body.replace("{{ action }}", "Gig Creator has removed  you from the Gig you where assigned to .\nGig Title : "+gigFetched.getTitle()+" .");
            this.emailSenderService.sendEmailWithAttachmentHtmlBodyOrNoOk(appChosen.getOwnerUser().getEmail(),"You Have Been Removed From A Gig Job", finalBody,"noReply@ghostech.com",null,true);

        }
        Gig gig = this.serviceGig.saveGig(gigFetched);

        if(gig!=null)
            this.serviceGig.gigViewDataUpdateBeforeReturn(gig);

        return true;
    }

}
