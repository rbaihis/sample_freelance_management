package com.example.user.repository.freelance;


import com.example.user.Entities.freelance.Gig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GigRepository extends JpaRepository<Gig,Long> {

    public Optional<Gig> findGigByFilesId(Long fileId);

    Page<Gig> findByMaxPriceBetween(double minCost, double maxCost, Pageable pageable);

}
