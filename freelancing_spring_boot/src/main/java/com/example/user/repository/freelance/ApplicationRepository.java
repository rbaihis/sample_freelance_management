package com.example.user.repository.freelance;

import com.example.user.Entities.freelance.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application,Long> {
    public Optional<Application> findApplicationByFilesId(Long fileId);

}
