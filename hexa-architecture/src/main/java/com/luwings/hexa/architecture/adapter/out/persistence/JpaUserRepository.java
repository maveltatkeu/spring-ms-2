package com.luwings.hexa.architecture.adapter.out.persistence;

import com.luwings.hexa.architecture.adapter.UserEntity;
import com.luwings.hexa.architecture.domain.port.out.UserRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: korawings : Mavel
 * Date: Sun 21 / Sep / 2025
 * Time: 23 : 36
 */
@Repository
public class JpaUserRepository implements UserRepository {

  private final SpringDataJpaUserRepository springDataJpaUserRepository;

  public JpaUserRepository(SpringDataJpaUserRepository springDataJpaUserRepository) {
    this.springDataJpaUserRepository = springDataJpaUserRepository;
  }
  @Override
  public UserEntity save(UserEntity user) {
    return springDataJpaUserRepository.save(user);
  }
}
// Spring Data JPA repository interface
