package com.tngtied.triplaner.member.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tngtied.triplaner.trip.entity.Plan;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

//entity를 validator에 사용하는 것에 대한??

@Entity
@Table(name = "MEMBER")
@Getter
@AllArgsConstructor
@SequenceGenerator(sequenceName = "MEMBER_SEQ", name = "memberSeq", allocationSize = 50, initialValue = 1)
public class Member implements UserDetails {

	@Id
	@GeneratedValue
	@Column(name = "USER_ID")
	private Long userId;

	@Column(unique = true, name = "USERNAME")
	@NotNull
	@Size(min = 2, max = 8)
	private String username;

	@Column(name = "PASSWORD")
	@NotNull
	private String password;

	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> roles = new ArrayList<>();

	@NotNull
	@Email
	@Column(name = "EMAIL")
	private String email;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
	@JsonManagedReference
	private List<Plan> plans;

	public Member() {
	}

	public Member(String username, String password, String role, String email) {
		this.username = username;
		this.password = password;
		this.roles = new ArrayList<>();
		this.roles.add(role);
		this.email = email;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles.stream()
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList());
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String toString() {
		String returnString = "username: " + this.username
			+ " | email: " + this.email
			+ " | roles: " + this.roles.toString();
		return returnString;
	}

}
