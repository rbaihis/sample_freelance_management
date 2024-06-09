package com.example.user.repository.freelance;

import com.example.user.Entities.freelance.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MilestoneRepository extends JpaRepository<Milestone,Long> {
    List<Milestone> findAllByGig_OwnerUser_Id(Long ownerUserId);
    List<Milestone> findAllByApplicationWon_OwnerUser_Id(Long ownerUserId);
}
