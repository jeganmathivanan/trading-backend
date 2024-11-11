package com.jegan.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jegan.Domain.USER_ROLE;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "fullName")
    private String fullName;


    @Column(name = "email")
    private String email;

    @JsonProperty(access= JsonProperty.Access.WRITE_ONLY )
    private String password;

    @Embedded
    private TwoFactorAuth twoFactorAuth = new TwoFactorAuth();

    private USER_ROLE role = USER_ROLE.ROLE_CUSTOMER;

}
