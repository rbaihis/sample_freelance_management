package com.example.user.interfaces.freelance;

import com.example.user.Entities.freelance.Application;
import com.example.user.Entities.freelance.Gig;


public interface IMilestoneGigApplicationService {



    public void assignApplicationWonToGigIsMileStones (Gig gig , Application applicationWon) ;
    public void unAssignApplicationWonFromGigIsMileStones(Gig gig ) ;

    /**
     * serves to update fields in application and gig to tell that a certain application has won the gig
     * also internally has logic toUpdate milestones by adding the ApplicationWonToIt or turning it back to null if removed
     * @param gigFetched type.Gig id of gig to fetch
     * @param idApplication Type.Long id Of ApplicationWonTheGig work
     * @return the Gig After update occurred in both applicationWon and Gig
     */
    public boolean ToggleAssignUnassignApplicationToGig( Gig gigFetched , Long idApplication);



}
