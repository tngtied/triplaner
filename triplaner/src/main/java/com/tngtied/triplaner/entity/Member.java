package com.tngtied.triplaner.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

//entity를 validator에 사용하는 것에 대한??

@Entity
@Table(name = "MEMBER")
@Getter
@AllArgsConstructor
public class Member implements UserDetails {

    @Id
    @GeneratedValue
    @Column(name="USERID")
    private Long UserId;

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
    @Column(unique = true, name = "EMAIL")
    private String email;

    public Member(String username, String password, String role, String email){
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

}
