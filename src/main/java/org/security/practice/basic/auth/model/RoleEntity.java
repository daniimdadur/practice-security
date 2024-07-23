package org.security.practice.basic.auth.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.security.practice.basic.util.CommonUtil;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_role")
public class RoleEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "name", length = 64)
    private String name;

    public RoleEntity(String name) {
        this.id = CommonUtil.getUUID();
        this.name = name;
    }
}
