package com.example.user.interfaces.freelance;

import com.example.user.dto.freelance.MileStoneUpdateDto;
import com.example.user.dto.freelance.RefundResponse;
import com.example.user.Entities.freelance.Milestone;
import com.example.user.Entities.freelance.enums.RefundOption;
import java.io.IOException;
import java.util.List;

public interface IMilestoneService {

    Milestone getOneMilestone (Long id) ;
    //-----------------------------

    void updateViewMilestoneFieldsBeforeReturn(Milestone ms);

    /**
     * is to check an update is necessary for the attribute milestone.isUpdatable
     * most use case is to update the object before fetching it to user since no cronJob is running to do this instead
     * @param ms type.Milestone
     * @return true if update have occurred
     */
    boolean isUpdateTheUpdatableAttributeBeforeReturnToUsersNoDbSaving(Milestone ms);
    //-----------------------------
    /**
     * get all milestones and update their Updatable Status before return it to user ;
     * just for test purpose or for admin(do it with pagination Better) or for Scripts to launched at midnight for updating milestones
     * this method consumes so much DB calls O(n)
     * it's a slow method should not be server for end users
     * avoid calling it in busy times with cronJobs
     * @return listMileStones with their Attribute isUpdatable update if needed
     */
    List<Milestone> getMilestones () ;




    //--------------------------------

    //---------------------------------------------------------
    List<Milestone> getMyMilestones(Long id);

    //---------------------------------------------------------
    List<Milestone> getMyAssignedMilestones(Long id);

    void addMilestoneSystemAttributeWithoutSaving (Milestone milestone) ;

    /**
     * Update a milestone to the DB
     * initial milestone is Always 10% of the totalCast of the winning Bid;
     * ps: !!! before using this method make sure the field milestone.setApplication and .setGig are filled
     * @param milestone type.MileStone
     * @return milestone  Saved after update
     */
    Milestone updateAddFilesToMileStone (Long id, MileStoneUpdateDto milestone) throws IOException;
    /**
     *
     * @param id type.Long id of MileStone
     * @return true if deleted else bad request
     */
    boolean deleteMilestone (Long id) ;
    /**
     * return true is mileStoneCan be deleted since No Application is assigned to it
     * @param ms type.MileStone
     * @return true if Updatable MileStone Can Be Deleted else false
     */
    boolean isMileStoneDeletable (Milestone ms);
    //---------------------------------------
    /**
     * Note!!: before calling Make Sure that Wallets Are Saved in Application && Gig
     * Application & Gig should Validate if Wallets are defined
     * @param id type.Long id of MileStone
     * @return true if mileStone is Activated
     */
    boolean activateMileStone(Long id);

    //-----------------------------------------------------
    /**
     * Note!!: before calling Make Sure that Wallets Are Saved in Application && Gig
     * Application & Gig should Validate if Wallets are defined
     * @param id type.Long id_mileStone
     * @return true if validated and amount is transferred to freelancer
     */
    boolean validateMileStoneWork(Long id);

    //-----------------------------------
    /**
     * Note!!: before calling Make Sure that Wallets Are Saved in Application && Gig
     * Application & Gig should Validate if Wallets are defined
     * @param id type.Long id_mileStone
     * @return true is reporting is accepted
     */
    boolean reportMileStone_customerNotSatisfied(Long id);
    //-------------------------------------
    /**
     * will do refund if applicable and return a custom-object type.RefundResponse
     * @param id Type.Long id_mileStone
     * @return  return type.RefundResponse(boolean => indicateSuccess , String ==> indicates Reason)
     */
    RefundResponse doRefundOrPayment(Long id ) ;
    //--------------------------------------
    /**
     * check if mileStone Can be canceled
     * @param ms type.MileStone
     * @return null_ifNoDepositInTheFirstCase_MethodCalledMistakenlyByDeveloper , Type.RefundOption based on Case , can return REQUIRES_THIRD_PARTY_INTERVENTION_OR_TICKET_RAISED if_logic_fails_to_solve_dispute
     */
    RefundOption MilestoneRefundTransferCaseResults (Milestone ms );

    boolean validateWorkDoneByApplicationWon(Long id);
}
