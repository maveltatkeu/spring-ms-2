package com.batch.batchalibou.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by IntelliJ IDEA.
 * User: korawings : Mavel
 * Date: Sat 06 / Dec / 2025
 * Time: 17 : 45
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Version
  private int version; // Hibernate automatically manages this field
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;
  private String country;
  private String gender;
  private String birthDate;
}
