package com.example.user.services.freelance;

import com.example.user.dto.freelance.MileStoneGetDto;
import com.example.user.Entities.freelance.FileDoc;
import com.example.user.Entities.freelance.Gig;
import com.example.user.Entities.freelance.Milestone;
import com.example.user.interfaces.freelance.IGigMilestoneService;
import com.example.user.interfaces.freelance.IGigService;
import com.example.user.repository.freelance.GigRepository;
import com.example.user.repository.freelance.MilestoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class GigMilestoneService implements IGigMilestoneService {
    private final GigRepository repoGig;
    private final MilestoneRepository repoMilestone;
    private final IGigService serviceGig;
    private final MilestoneService serviceMilestone;
    private final FileDocService fileDocService;

    @Autowired
    public GigMilestoneService(GigRepository repoGig, MilestoneRepository repoMilestone, IGigService serviceGig, MilestoneService serviceMilestone, FileDocService fileDocService) {
        this.repoGig = repoGig;
        this.repoMilestone = repoMilestone;
        this.serviceGig = serviceGig;
        this.serviceMilestone = serviceMilestone;
        this.fileDocService = fileDocService;
    }
    //************************************************

    /* do not use not finished*/
    @Override
    public List<MileStoneGetDto> getGigMileStones (Long idGig) {
        Gig gigFetched = repoGig.findById(idGig).orElse(null);
        if (gigFetched == null)
            return null;

        //this can be omitted if we use cronJob which do the Job at Midnight everyday
        List<Milestone> milestones= gigFetched.getMilestones();
        List<MileStoneGetDto> milestonesDto = new ArrayList<>();
        milestones.forEach( (ms) -> {
            if(this.serviceMilestone.isUpdateTheUpdatableAttributeBeforeReturnToUsersNoDbSaving(ms))
                this.repoMilestone.save(ms);
            milestonesDto.add( MileStoneGetDto.EntityMapperToDto(ms) );
        });

        return milestonesDto ;
    }

    //------------------------------
    @Override
    public boolean isGigMileStonesSumUpTo100Percent_plusAffectingAllSystemNecessaryAttributes(Gig gig, List<Milestone> milestones){

        int sum = 0;
        for (Milestone ms : milestones) {
            ms.setGig(gig);
            this.serviceMilestone.addMilestoneSystemAttributeWithoutSaving(ms);
            sum += ms.getAmountPercentage();
        }
        return sum == 100;
    }
    //-------------------------------
    /**
     * adding gig and Save its uploaded Files in System
     * @param gig type.Gig
     * @return Gig  the saved gig with its id generated
     * @throws IOException if a problem occurs  when saving files
     */
    @Override
    public Gig addGig (Gig gig) throws IOException {

        //we only allow gigs with its mileStones
        if (gig.getMilestones() == null)
            return null;

        //this will also add milestone.gig = gig and other SystemAutoAddAttributes, to not waist looping time for adding and for calculating separately
        if( !this.isGigMileStonesSumUpTo100Percent_plusAffectingAllSystemNecessaryAttributes(gig, gig.getMilestones()))
            return null; //bad request (milestone Amount Percentage Has wrong Values)


        //related to gig
        if (gig.getFiles() != null)
            for (FileDoc file : gig.getFiles()) {
                //----
                this.fileDocService.addUniqueFileName(file);
                this.fileDocService.saveOnePhyFile(file);
            }

        Gig gigToReturn  =this.serviceGig.saveGig(gig);
        this.serviceGig.gigViewDataUpdateBeforeReturn(gigToReturn);

        return gigToReturn;
    }
}
