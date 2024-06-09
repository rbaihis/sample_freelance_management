package com.example.user.dto.freelance;

import com.example.user.Entities.freelance.FileDoc;
import com.example.user.Entities.freelance.Milestone;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MileStoneGetDto {

    private Long id;
    private Long idGig;
    private Long idApplicationWon;
    private Long ownerId;
    private Long freelancerId;

    private String goalDescription;
    private Integer amountPercentage;
    //this is calculated not an attribute in the original entity
    private Float amount ;
    private LocalDateTime dateCreation ;
    private LocalDate dateStartWorking;
    private LocalDate datePayment;
    //critical for Update
    private boolean isUpdatable ;
    //critical for Delete and refund based On those and datePayment
    private boolean isDeposited ; // means also mileStone is Activated
    private boolean isValidatedByGigCreator ;
    private boolean isValidatedByFreelancer ;
    private boolean isReclamationByGigCreator ;

    //walletsIds:
    private String walletCustomer;
    private String walletFreelancer;
    //10 is max score
    private Float ratingFreelancer ;
    //10 is max score
    private Float ratingCustomer ;
    private List<FileDoc> milestoneDeliverable = new ArrayList<>();

    public static MileStoneGetDto EntityMapperToDto(Milestone ms ){
        MileStoneGetDto msDto = new MileStoneGetDto();
        if (ms == null )
            return null;


        if (ms.getId() != null) {
            msDto.setId(ms.getId());
        }
        //------
        if (ms.getGig() != null) {
            //set Id Gig
            msDto.setIdGig(ms.getGig().getId());

            //set
            msDto.setWalletCustomer(
                    ms.getGig().getWalletId() != null ?
                            ms.getGig().getWalletId() : "");
        }
        //-------
        if (ms.getApplicationWon() != null) {

            //get id
            msDto.setIdApplicationWon(ms.getApplicationWon().getId());

            //getWalletId
            msDto.setWalletFreelancer(
                    ms.getApplicationWon().getWalletId() != null ?
                            ms.getApplicationWon().getWalletId() : ""
            );

            //CalculateAmountOFMileStone
            msDto.setAmount(
                    ms.getAmountPercentage()*ms.getApplicationWon().getBid()
            );
        }


        if (ms.getGoalDescription() != null) {
            msDto.setGoalDescription(ms.getGoalDescription());
        }
        if (ms.getAmountPercentage() != null) {
            msDto.setAmountPercentage(ms.getAmountPercentage());
        }
        if (ms.getDatePayment() != null) {
            msDto.setDatePayment(ms.getDatePayment());
        }
        if (ms.getDateStartWorking() != null) {
            msDto.setDateStartWorking(ms.getDateStartWorking());
        }
        if (ms.getDateCreation() != null) {
            msDto.setDateCreation(ms.getDateCreation());
        }
        if (ms.getRatingCustomer() != null) {
            msDto.setRatingCustomer(ms.getRatingCustomer());
        }
        if (ms.getRatingFreelancer() != null) {
            msDto.setRatingFreelancer(ms.getRatingFreelancer());
        }



        //ms-boolean can't be null using primitive
        msDto.setUpdatable(ms.isUpdatable());
        msDto.setDeposited(ms.isDeposited());
        msDto.setValidatedByFreelancer(ms.isValidatedByFreelancer());
        msDto.setValidatedByGigCreator(ms.isValidatedByGigCreator());
        msDto.setReclamationByGigCreator(ms.isReclamationByGigCreator());

        //those needed if Freelancer needs to download a file or delete it
        if (ms.getMilestoneDeliverable() != null )
            msDto.getMilestoneDeliverable().addAll(ms.getMilestoneDeliverable());

        return msDto;
    }
}
