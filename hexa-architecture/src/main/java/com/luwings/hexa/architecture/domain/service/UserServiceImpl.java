package com.luwings.hexa.architecture.domain.service;

import com.luwings.hexa.architecture.domain.model.User;
import com.luwings.hexa.architecture.domain.port.in.UserService;
import com.luwings.hexa.architecture.domain.port.out.UserRepository;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: korawings : Mavel
 * Date: Sun 21 / Sep / 2025
 * Time: 23 : 34
 */
@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }
  @Override
  public User createUser(User user) {
    // Apply business rules here
    return userRepository.save(user);
  }
}
