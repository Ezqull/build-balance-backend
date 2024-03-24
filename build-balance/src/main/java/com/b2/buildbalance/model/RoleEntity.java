package com.b2.buildbalance.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@Table(name = "role", schema = "build_balance")
@EqualsAndHashCode(callSuper = true)
public class RoleEntity extends BaseEntity {

    @Column(name = "name")
    private String name;

    public RoleEntity(String name) {
        super();
        this.name = name;
    }
}
