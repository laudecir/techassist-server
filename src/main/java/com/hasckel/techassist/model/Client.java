package com.hasckel.techassist.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Entity
@Table(name = "client")
public class Client extends BaseModel {

    @NotEmpty
    private String name;

    @NotEmpty
    private String address;

    @NotEmpty
    private String phone;

    @Email
    @NotEmpty
    private String email;

}
