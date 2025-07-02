package com.cts.userservice.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	
	@NotBlank
//	@Column(unique = true)
	private String name;
	
	@NotBlank
	@Email
	@Column(unique = true)
	private String email;
	
	@Pattern(message ="Password must have atleast 8 char long with atleast 1 Alphabet, 1 special Char, and 1 Number", 
			regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?`~])(?=.*[^\\s]).{8,}$")
	private String password;
	
	private String role;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
	private boolean isDeleted;

	@JsonManagedReference
	@OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private Profile profile;

}

