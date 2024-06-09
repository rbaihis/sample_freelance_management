package com.example.user.Entities.freelance;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class FileDoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String extension;
    private String path;
    private boolean isSaved = false;
    @Transient // field below will be ignored by JPA
    private String content;


}
