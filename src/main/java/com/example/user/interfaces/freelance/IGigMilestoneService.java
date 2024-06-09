package com.example.user.interfaces.freelance;

import com.example.user.dto.freelance.MileStoneGetDto;
import com.example.user.Entities.freelance.Gig;
import com.example.user.Entities.freelance.Milestone;

import java.io.IOException;
import java.util.List;

public interface IGigMilestoneService {
    public List<MileStoneGetDto> getGigMileStones (Long idGig) ;

    public Gig addGig (Gig gig) throws IOException;

    //-----------------------------------------
    public boolean isGigMileStonesSumUpTo100Percent_plusAffectingAllSystemNecessaryAttributes(Gig gig, List<Milestone> milestones) ;
}
