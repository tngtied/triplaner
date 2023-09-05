package com.tngtied.triplaner.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

//entity를 validator에 사용하는 것에 대한??

@Entity
@Table(name = "USER")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue
    @Column(name="USERID")
    private Long UserId;

    @Column(unique = true, name = "USERNAME")
    @NotNull
    private String username;

    @Column(name = "PASSWORD")
    @NotNull
    private String password;

    @NotNull
    @Email
    @Column(unique = true, name = "EMAIL")
    private String email;

}
