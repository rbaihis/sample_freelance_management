package com.example.user.repository;

import com.example.user.Entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository  extends JpaRepository<AppUser,Long> {

}
