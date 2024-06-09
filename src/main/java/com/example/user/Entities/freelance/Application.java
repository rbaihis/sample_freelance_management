package com.example.user.Entities.freelance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.example.user.Entities.AppUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Application {
    // not saved data
    @Transient
    private Long gigId;
    @Transient
    private Long ownerId;
    @Transient
    private Long customerId;
    @Transient
    private String ownerName;

    //-----User Relationship---------
    @ManyToOne
    @JsonIgnore
    private AppUser ownerUser;
    //------db-data---------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String freelancer;
    private String freelancerProfile;
    private String description;
    private String questions;
    // add comma ',' in between purpose: is to have array of links stored in one string separated by ,
    private String workReferenceLinks;
    private Float bid;
    private Float extraCost;
    private String walletId;
    private LocalDateTime submitDate = LocalDateTime.now();
    private boolean isApplicationWon= false;

//  Relationships:
    @OneToMany(cascade = CascadeType.ALL )
    private List<FileDoc> files;
//  Main Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Gig gig;
//    @ManyToOne  private User freelancer;


    @Override
    public int hashCode(){
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Application that)) return false;
        return Objects.equals(id, that.id);
    }


}
