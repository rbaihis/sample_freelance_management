package com.example.user.services.freelance;

import com.example.user.dto.freelance.MileStoneUpdateDto;
import com.example.user.dto.freelance.RefundResponse;
import com.example.user.Entities.freelance.FileDoc;
import com.example.user.Entities.freelance.Milestone;
import com.example.user.Entities.freelance.enums.RefundOption;
import com.example.user.interfaces.freelance.IFileDocService;
import com.example.user.interfaces.freelance.IMilestoneService;
import com.example.user.repository.freelance.MilestoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class MilestoneService implements IMilestoneService {
    private final MilestoneRepository repo;
    private final IFileDocService serviceFileDoc;
    @Autowired
    MilestoneService(MilestoneRepository repo, IFileDocService serviceFileDoc){
        this.repo = repo;
        this.serviceFileDoc = serviceFileDoc;
    }
    //-***************************************************

    @Override
    public void updateViewMilestoneFieldsBeforeReturn(Milestone ms){
        if(ms.getApplicationWon()!= null  && ms.getApplicationWon().getOwnerUser()!=null)
            ms.setFreelancerId(ms.getApplicationWon().getOwnerUser().getId());


        if(ms.getGig().getOwnerUser() != null){
            ms.setOwnerId(ms.getGig().getOwnerUser().getId());
        }
        if(ms.getApplicationWon()!= null  && ms.getApplicationWon().getWalletId()!= null)
            ms.setWalletFreelancer(ms.getApplicationWon().getWalletId());

        if(ms.getGig().getWalletId() != null ){
            ms.setWalletCustomer(ms.getGig().getWalletId());
        }

        ms.setGigId(ms.getGig().getId());
    }

    @Override
    public boolean isUpdateTheUpdatableAttributeBeforeReturnToUsersNoDbSaving(Milestone ms){

        if(ms.getDatePayment().isBefore(LocalDate.now()) && ms.isUpdatable()){
            ms.setUpdatable(false);
            //repo.save(ms); no DB Saving to optimise responseTime to user is some Scenarios
            //update attribute in db while freelancer try to update after deadline
            return true;
        }
        return false;
    }

    //-----------------------------
    @Override
    public Milestone getOneMilestone (Long id) {
        Milestone ms =repo.findById(id).orElse(null);
        if (ms == null)
            return null;

        // should be Verified if Updatable or not To Avoid Freelancer Submit Late Files and then asks for Payment
        if(this.isUpdateTheUpdatableAttributeBeforeReturnToUsersNoDbSaving(ms))
            this.repo.save(ms);

        this.updateViewMilestoneFieldsBeforeReturn(ms);

        return ms;

    }
    //-----------------------------


    /**
     * get all milestones and update their Updatable Status before return it to user ;
     * just for test purpose or for admin(do it with pagination Better) or for Scripts to launched at midnight for updating milestones
     * this method consumes so much DB calls O(n)
     * it's a slow method should not be server for end users
     * avoid calling it in busy times with cronJobs
     * @return listMileStones with their Attribute isUpdatable update if needed
     */
    @Override
    public List<Milestone> getMilestones () {
        /*
        add logic to verify user's role can Read gigs (notNeeded if Using SpringSecurity Options)
        if(loggedUser.role.equals(Role.GIG_Owner || Role.Application_Accepted ))
        */
        List<Milestone> milestones= repo.findAll();
        milestones.forEach( (ms) -> {
            if(this.isUpdateTheUpdatableAttributeBeforeReturnToUsersNoDbSaving(ms))
                this.repo.save(ms);
        });

        for (Milestone ms : milestones)
            updateViewMilestoneFieldsBeforeReturn(ms);

        return milestones;
    }
//---------------------------------------------------------
    @Override
    public List<Milestone> getMyMilestones(Long id) {
        /*
        add logic to verify user's role can Read gigs (notNeeded if Using SpringSecurity Options)
        if(loggedUser.role.equals(Role.GIG_Owner || Role.Application_Accepted ))
        */
        List<Milestone> milestones= repo.findAllByGig_OwnerUser_Id(id);
        milestones.forEach( (ms) -> {
            if(this.isUpdateTheUpdatableAttributeBeforeReturnToUsersNoDbSaving(ms))
                this.repo.save(ms);
        });

        for (Milestone ms : milestones)
            updateViewMilestoneFieldsBeforeReturn(ms);

        return milestones;
    }
//---------------------------------------------------------
    @Override
    public List<Milestone> getMyAssignedMilestones(Long id) {
        /*
        add logic to verify user's role can Read gigs (notNeeded if Using SpringSecurity Options)
        if(loggedUser.role.equals(Role.GIG_Owner || Role.Application_Accepted ))
        */
        List<Milestone> milestones= repo.findAllByApplicationWon_OwnerUser_Id(id);
        milestones.forEach( (ms) -> {
            if(this.isUpdateTheUpdatableAttributeBeforeReturnToUsersNoDbSaving(ms))
                this.repo.save(ms);
        });

        for (Milestone ms : milestones)
            updateViewMilestoneFieldsBeforeReturn(ms);

        return milestones;
    }
//---------------------------------------------------------


    /**
     * add a milestoneSystemAutoAdded fields that endUser don't Know about or for security
     * initial milestone is Always 10% of the totalCast of the winning Bid;
     * ps: !!! before using this method make sure the field milestone.setGig() is filled
     * @param milestone type.MileStone
     */
    @Override
    public void addMilestoneSystemAttributeWithoutSaving (Milestone milestone) {

        //fields to set by system
        //no files To handel here since Creator will see filesAdded by freelancer not add them himself
        milestone.setUpdatable(false);
        milestone.setDeposited(false);
        milestone.setValidatedByFreelancer(false);
        milestone.setValidatedByGigCreator(false);
        milestone.setRatingFreelancer(10f);
        milestone.setRatingCustomer(10f);
    }





//--------------------------------------------------------------------------

    /**
     * Update a milestone to the DB
     * initial milestone is Always 10% of the totalCast of the winning Bid;
     * ps: !!! before using this method make sure the field milestone.setApplication and .setGig are filled
     * @param milestone type.MileStone
     * @return milestone  Saved after update
     */
    @Override
    public Milestone updateAddFilesToMileStone (Long id, MileStoneUpdateDto milestone) throws IOException {
        Milestone fetchedMs = this.repo.findById(id).orElse(null);
        //no null pointer exception to worry since (milestone == null) is evaluated first.
        if ( fetchedMs == null )
            return null;

        if (!fetchedMs.isUpdatable())
            return null;


        if(  LocalDate.now().isAfter(fetchedMs.getDatePayment())  ) {
            fetchedMs.setUpdatable(false);
            this.repo.save(fetchedMs);
            return null;
        }

        if (milestone.getMilestoneDeliverable() != null)
            for (FileDoc file : milestone.getMilestoneDeliverable()) {
                this.serviceFileDoc.addUniqueFileName(file);
                this.serviceFileDoc.saveOnePhyFile(file);
                fetchedMs.getMilestoneDeliverable().add(file);
            }
        else /* debug purpose*/
            System.out.println("******\n\tNo Files TO Add to mileStone in this updateMilestoneAddFile Request \n**********");


        Milestone ms = repo.save(fetchedMs);

        this.updateViewMilestoneFieldsBeforeReturn(ms);

        return ms;
    }

    @Override
    public boolean deleteMilestone (Long id) {

        Milestone ms = repo.findById(id).orElse(null);
        if (ms == null) return false;

        boolean deletable = this.isMileStoneDeletable(ms);
        if(deletable)
            repo.delete(ms);

        return deletable ;
    }

    /**
     * return true is mileStoneCan be deleted since No Application is assigned to it
     * @param ms type.MileStone
     * @return true if Updatable MileStone Can Be Deleted else false
     */
    @Override
    public boolean isMileStoneDeletable (Milestone ms){
        //ps: we don't care if this deleted after deadline as long it has no relationship with an Application

        if ( ms.getApplicationWon() != null  )
            return false;
        else //debug-purpose
            System.out.println("""
                    !!!!!!!Error-In-Logic if :
                    \t updatable == true
                    \t\tNo_mileStone_Already_Created_Can_Be_Deletable is an a MileStone With applicationWon already Assigned To it
                    !!!!!!!
                    """);
        return  true;
    }


    //----------Actions of gig Creator----------------

    /**
     * Note!!: before calling Make Sure that Wallets Are Saved in Application && Gig
     * Application & Gig should Validate if Wallets are defined
     * @param id type.Long id of MileStone
     * @return true if mileStone is Activated
     */
    @Override
    public boolean activateMileStone(Long id){
        Milestone ms =this.repo.findById(id).orElse(null);
        if (ms == null)
            return false;

        /* check if user is GigCreator Of this mileStone before calling this*/
        //can't activate milestone if there is no freelancer working on it
        if(ms.getGig().getWalletId()==null ||
                ms.getApplicationWon()==null ||
                ms.getApplicationWon().getWalletId()==null
        ) return false;


        // deposit have been paid already  can't charge u twice ==> milestone Already activated
        if(ms.isDeposited())
            return false;

        //DepositTransactionLogicHere
        //ifApiAcceptTagsAddAttributeUniqueTag_toBeAddedToMileStoneEntity_forEasierSearch
        System.out.println("""
                *******************
                \ta Deposit transaction should be made now .
                \t\t ==> call a service for deposit Api now and for validate transaction have been received
                *******************
                """);
        //verifyTransactionHere received twice here
        System.out.println("""
        ---------------
        \ttest if deposit fails return false (verify_twice for latency potential bug in api used)
        ---------------
        """);

        //if above condition is met update fields
        ms.setDeposited(true);
        ms.setUpdatable(true);
        System.out.println("setting deposited & updatable to true");
        System.out.println("setting deposited & updatable allow freelancer to add its deliverable to the  milestone");

        //calculate Difference in date to have the new MileStone PaymentDate agreed case if milestoneActivated late
        long daysDifference = ChronoUnit.DAYS.between(ms.getDateStartWorking(), LocalDate.now());
        if(daysDifference>0)
            //update the payment Day to be fair with freelancer ==> since customer chose to activate it this means he is accepting it being late
            ms.setDateStartWorking(ms.getDateStartWorking().plusDays(daysDifference));

        this.repo.save(ms);

        return true;
    }


    //-----------------------------------------------------

    /**
     * Note!!: before calling Make Sure that Wallets Are Saved in Application && Gig
     * Application & Gig should Validate if Wallets are defined
     * @param id type.Long id_mileStone
     * @return true if validated and amount is transferred to freelancer
     */
    @Override
    public boolean validateMileStoneWork(Long id){
        Milestone ms =this.repo.findById(id).orElse(null);
        if (ms == null)
            return false;

        /* check if user is GigCreator Of this mileStone before calling this*/
        if(ms.getGig().getWalletId()==null || ms.getApplicationWon()==null  || ms.getApplicationWon().getWalletId()==null)
            return false;

        //can't transfer payment we haven't received in first place
        if(!ms.isDeposited())//
            return false;

        // this means this operation is called before and its transferAmount already occurred
        if(ms.isValidatedByGigCreator())
            return false;



        /*  ps: Gig && applicationWon must have WalletsId defined for this to work */
        System.out.println("""
                ********
                \t a payment Logic to Freelancer Should Be Raised now !!!
                ********
                """);
        //verifyTransactionHere received twice here
        System.out.println("""
        ---------------
        \ttest if deposit fails return false (verify_twice for latency potential bug in api used)
        ---------------
        """);

        //update attribute if condition above is met
        ms.setValidatedByGigCreator(true);


        this.repo.save(ms);
        return true;
    }

    //-----------------------------------

    /**
     * Note!!: before calling Make Sure that Wallets Are Saved in Application && Gig
     * Application & Gig should Validate if Wallets are defined
     * @param id type.Long id_mileStone
     * @return true is reporting is accepted
     */
    @Override
    public boolean reportMileStone_customerNotSatisfied(Long id){
        Milestone ms =this.repo.findById(id).orElse(null);
        if (ms == null)
            return false;

        /* check if user is GigCreator Of this mileStone before calling this*/
        if(ms.getGig().getWalletId()==null  ||
                ms.getApplicationWon()==null ||
                ms.getApplicationWon().getWalletId()==null
        )return false;

        //reclamation for what u didn't deposit anything freelancer has no obligation to work on it
        if (!ms.isDeposited())
            return false;

        // too late already validate by u or the system (case freelancer requested payment)
        //already considered that customer can ask for refund only after 3days of deadline passed
        if(ms.isValidatedByGigCreator())
            return false;

        //to late or too early for reclamation
        if (
                LocalDate.now().isBefore(ms.getDatePayment())
                || LocalDate.now().isAfter(ms.getDatePayment().plusDays(3))
       )return false;


        ms.setReclamationByGigCreator(true);

        return true;
    }
    //-----------Actions Of Freelancer Creator---------------
    public boolean validateWorkDoneByApplicationWon(Long id){
        Milestone ms =this.repo.findById(id).orElse(null);
        if ( ms == null )
            return false;

    //------------If any of those are reached this will indicate a bug exist ==> bad Validation @ Create------------
        // wallet ids are mandatory
        if ( ms.getGig() ==null  || ms.getApplicationWon()== null
                || ms.getGig().getWalletId()==null || ms.getApplicationWon().getWalletId()==null)
            return false;

        //freelancer should only work if mileStone is Activated and is (deposited ==>(updatable) ) ==>(therefore can't validate)
        if(!ms.isDeposited() ||  !ms.isUpdatable() )//
            return false;
    //--------------End-bugs-might-exist-------------------------


        // freelancer must submit his work else can't validate he did it
        if (ms.getMilestoneDeliverable().isEmpty())
            return false;

        // can Validate As Long No reclamation BY Gig Creator (deadline does not matter since he can finish late and agrees with GigCreator privately)
        if(ms.isReclamationByGigCreator())
            return false;


        ms.setValidatedByFreelancer(true);
        repo.save(ms);
        return true;
    }

    //-----------End Actions Of Freelancer Creator---------------



    //-----Refund Action by Freelancer Or GigCreator-----------------------
    /**
     * will do refund if applicable and return a custom-object type.RefundResponse
     * @param id Type.Long id_mileStone
     * @return  return type.RefundResponse(boolean => indicateSuccess , String ==> indicates Reason)
     *
     */
    @Override
    public RefundResponse doRefundOrPayment(Long id ) {
        Milestone msFetched = this.repo.findById(id).orElse(null);
        if (msFetched == null)
            return null;

        // one case is still missing for GigCreator that can refund after deadline
        return switch (this.MilestoneRefundTransferCaseResults(msFetched )) {
            case BAD_REQUEST -> RefundResponse.builder()
                    .isRefundable(false)
                    .message("""
                            !!!!!!!!
                            \tNo deposit for this MileStone in the first Place how re u expecting refund.
                            \tThis should not be printed in the first place indicates Bad logic (should be corrected).
                            !!!!!!!!
                            """)
                    .build();
            case ALREADY_VALIDATED_AND_PAYED_WRONG_CALL -> RefundResponse.builder()
                    .isRefundable(false)
                    .message("""
                            !!!!!
                            \tThis milestone have been already validated by you or by SystemPolicy and not eligible for refund .
                            \tThis should not be printed in the first place indicates Bad logic (should be corrected).
                            !!!!!!
                            """)
                    .build();
            case FIFTY_FIFTY -> {
                System.out.println("Do logic To Do transfer/refund");
                yield RefundResponse.builder()
                        .isRefundable(true)
                        .message("50/50 Amount Transferred/refunded to Freelancer/Customer after policy Validation")
                        .build();
            }
            case FAVORS_CUSTOMER -> {
                System.out.println("Do logic To Do Refund");
                yield RefundResponse.builder()
                        .isRefundable(true)
                        .message("Amount Transferred to Freelancer after policy Validation")
                        .build();
            }
            case FAVORS_FREELANCER -> {
                System.out.println("Do logic To Do transfer");
                yield RefundResponse.builder()
                        .isRefundable(true)
                        .message("Amount Transferred to Freelancer after Policy Validation")
                        .build();
            }
            case WAIT_FOR_MORE_DAYS_AND_REDO_OPERATION -> RefundResponse.builder()
                    .isRefundable(false)
                    .message("Please redo this operation When deadline +3days passed for Freelancers")
                    .build();
            case REQUIRES_THIRD_PARTY_INTERVENTION_OR_TICKET_RAISED -> RefundResponse.builder()
                    .isRefundable(false)
                    .message("""
                            !!!!!
                            \tPlease contact customer service to solve this dispute.
                            \tThis should not be printed in the first place indicates Bad logic (should be corrected).
                            !!!!!!
                            """)
                    .build();
        };

    }
    //-----End-------Refund Action by Freelancer Or GigCreator-----------------------


    //----------Private methods ( change public to private methods below later)----------
    /**
     * check if mileStone Can be canceled
     * @param ms type.MileStone
     * @return null_ifNoDepositInTheFirstCase_MethodCalledMistakenlyByDeveloper , Type.RefundOption based on Case , can return REQUIRES_THIRD_PARTY_INTERVENTION_OR_TICKET_RAISED if_logic_fails_to_solve_dispute
     */
    @Override
    public RefundOption MilestoneRefundTransferCaseResults (Milestone ms ){
        //wrong call of method by developer should test if milestone isDeposited first before calling this method
        if (!ms.isDeposited())
            return RefundOption.BAD_REQUEST;

        // already amount transferred to freelancer since all what matters is Validation by Customer For Payment
        if (ms.isValidatedByGigCreator())
            return RefundOption.ALREADY_VALIDATED_AND_PAYED_WRONG_CALL;
/*
    a guaranteed deposit by customer has been made in all Cases below:
 */

        if ( LocalDate.now().isBefore(ms.getDatePayment()) )
            return RefundOption.WAIT_FOR_MORE_DAYS_AND_REDO_OPERATION;

    //deadline_passed && deposit by customer has been made Cases :
        //1st Criteria :  Favors_Customer
            // no Work_Submitted_By_freelancer && NotValidatedByCustomer && NotValidatedByFreelancer
        if (
                // && user is customer (add this logic anywhere u see fit)
                 !ms.isValidatedByFreelancer()
                && ms.getMilestoneDeliverable().isEmpty()
        ) return RefundOption.FAVORS_CUSTOMER;



        //2nd Criteria : Favors_Freelancer
            // deadline passed+3Days + Not Validated by Freelancer + Validated by Customer + WorkHaveBeenSubmitted
        if (
                //&& user.IsOwnerOfThisApplication (add this logic anywhere u see fit)
                ms.getDatePayment().plusDays(3).isAfter(LocalDate.now())
                && !ms.isReclamationByGigCreator()
                //&& !ms.isValidatedByGigCreator() no need already factored above in if statement
                && ms.isValidatedByFreelancer()
                && !ms.getMilestoneDeliverable().isEmpty()
        ) return RefundOption.FAVORS_FREELANCER;

        //should be TicketCases raised not Direct-refund
        if(
                //&& user.isApplicationOwner || user.isGigCreator (add this logic anywhere u see fit)
                ms.getDatePayment().plusDays(3).isAfter(LocalDate.now())
                &&  ms.isReclamationByGigCreator()
                && !ms.isValidatedByGigCreator()
                &&  ms.isValidatedByFreelancer()
                && !ms.getMilestoneDeliverable().isEmpty()
        ) return RefundOption.FIFTY_FIFTY;


        System.out.println("""
                *****************
                \tCase Requires Ticket Maybe:
                    \t\tReasons:
                        \t\t1-Logic fails to Solve the Problem logic-bug
                        \t\t2-a condition occurred we haven't Thought about it ;
                *****************
                """);
        System.out.println("------------\n+"
                +"\t Entity details: \n\t\t"+ms
                +"\n-------------"
        );

        return RefundOption.REQUIRES_THIRD_PARTY_INTERVENTION_OR_TICKET_RAISED;
    }

    //-------End private methods------------------------------


}
