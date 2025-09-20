package com.luwings.springsecurityhttpbasicauth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "authorities")
public class AuthorityEntity {
  @Id
  @GeneratedValue
  private Long id;
  @Column
  private String name;

  @ManyToMany(mappedBy = "authorities")
  private Set<RoleEntity> roleEntities;

  @Override
  public String toString() {
    return "Authority{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", roles=" + roleEntities +
        '}';
  }
}