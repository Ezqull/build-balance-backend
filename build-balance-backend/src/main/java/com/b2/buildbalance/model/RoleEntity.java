package com.b2.buildbalance.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "Role")
@EqualsAndHashCode(callSuper = true)
public class RoleEntity extends BaseEntity {
    @Column(name = "name")
    private String name;

    public RoleEntity(String name) {
        super();
        this.name = name;
    }
}
