package com.example.user.Entities.freelance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Milestone {
    //---------extra---------
    @Transient
    private Long ownerId;
    @Transient
    private Long freelancerId;
    @Transient
    private Long gigId;
    @Transient
    private String walletCustomer;
    @Transient
    private String walletFreelancer;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String goalDescription;
    private Integer amountPercentage ;
    private LocalDateTime dateCreation = LocalDateTime.now();
    private LocalDate dateStartWorking;
    private LocalDate datePayment;
    //critical for Update ()
    // false means no applicationWon assigned yet or MileStone Deadline has been Reached
    private boolean isUpdatable = false;
    //critical for Delete and refund based On those and datePayment
    private boolean isDeposited = false; // means also mileStone is Activated
    //!!!this can be toggled to true only once witch initiate payment to freelancer
    private boolean isValidatedByGigCreator  = false;
    private boolean isValidatedByFreelancer = false;
    private boolean isReclamationByGigCreator  = false;

    //10 is max score
    private Float ratingFreelancer = 10f;
    //10 is max score
    private Float ratingCustomer = 10f;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FileDoc> milestoneDeliverable;

    @ManyToOne
    @JsonIgnore
    private Gig gig;

    @ManyToOne//unidirectional for now
    private Application applicationWon;

//    private Application chosenApplication;
    @Override
    public int hashCode(){
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Milestone milestone)) return false;
        return Objects.equals(id, milestone.id);
    }


}
