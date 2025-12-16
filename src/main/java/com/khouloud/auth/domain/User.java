package com.khouloud.auth.domain;

import java.util.Date;

 

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Table(name="Users")
@Entity
public class User/* implements UserDetails*/ {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long Id;
	@Column(nullable = false)
	private String fullName;
	@Column(nullable = false)
	private String email;
	@Column(nullable = false)
	private String password;
	@Column
	private Date createdAt;
	@Column
	private Date updatedAt;
}
