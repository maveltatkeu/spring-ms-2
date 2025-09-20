package com.luwings.springsecurityhttpbasicauth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "roles")
public class RoleEntity {
  @Id
  @GeneratedValue
  private Long id;

  @Column
  private String name;

  @ManyToMany(mappedBy = "roleEntities")
  private Set<UserEntity> userEntities;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "role_authorities",
      joinColumns = @JoinColumn(name = "role_id"),
      inverseJoinColumns = @JoinColumn(name = "authority_id")
  )
  private Set<AuthorityEntity> authorities;

  @Override
  public String toString() {
    return "Role{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", users=" + userEntities +
        ", authorities=" + authorities +
        '}';
  }
}