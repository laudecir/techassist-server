package com.hasckel.techassist.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "role")
public class Role extends BaseModel implements GrantedAuthority {

    public static enum RoleType {
        ADMIN,
        RECEPCIONIST,
        TECHNICAL;
    }

    private RoleType type;

    @Override
    @JsonIgnore
    public String getAuthority() {
        return "ROLE_" + type.name();
    }

}
