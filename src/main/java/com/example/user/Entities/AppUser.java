package com.example.user.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Table(name = "sec_users")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private  String name;
    private  String password;
    private  String email;
    @ManyToMany(fetch = FetchType.EAGER)//fetch the roles collection whenever an AppUser is loaded. This ensures that the roles are loaded along with the AppUser entity and prevents the LazyInitializationException
    @JoinTable(name="sec_user_roles"
            ,joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"))
    @OrderColumn(name="id")
    private Set<Role> roles =new HashSet<>();//is a collection that does not allow duplicate elements
}