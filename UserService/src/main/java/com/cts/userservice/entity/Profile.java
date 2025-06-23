package com.cts.userservice.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    private String bio;

    @Column(length = 15)
    private String phoneNumber;

    private String address;

//    @Enumerated(EnumType.STRING)
//    private Visibility visibility; // PUBLIC, PRIVATE, FRIENDS_ONLY

//    private int profileCompletion; // %

    private boolean isDeleted; // Soft delete for profile

    private String profileImageUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "user_id",
            referencedColumnName = "userId",
            foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT)
    )
    @JsonBackReference
    private User user;

}
