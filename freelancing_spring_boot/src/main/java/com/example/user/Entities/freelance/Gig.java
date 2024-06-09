package com.example.user.Entities.freelance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.example.user.Entities.AppUser;
import com.example.user.listeners.GigDeletionListener;
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
@NoArgsConstructor(force = true)
@Builder
@EntityListeners(GigDeletionListener.class)
public class Gig {
    // not saved data
    @Transient
    private Long ownerId;
    @Transient
    private String ownerName;

    //-------user relationship-------
    @ManyToOne
    @JsonIgnore
    private AppUser ownerUser;
    //- saved data in db
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String questions;
    private float minPrice;
    private float maxPrice;
    private LocalDateTime submitDate = LocalDateTime.now();
    private LocalDate projectStart ;
    private LocalDate projectDeadline ;
    private boolean isGigAssigned = false;
    private String walletId;
    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Skill> skills;

    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FileDoc> files;

    @OneToMany(mappedBy = "gig", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
                        private List<Application> applications;

    @OneToMany(mappedBy = "gig", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Milestone> milestones;

//    @ManyToOne  private User freelancer;
//    @JsonIgnore

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Gig gig)) return false;
        return Objects.equals(id, gig.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
