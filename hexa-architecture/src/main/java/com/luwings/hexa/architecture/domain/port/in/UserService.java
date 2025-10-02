package com.luwings.hexa.architecture.domain.port.in;

import com.luwings.hexa.architecture.domain.model.User;

/**
 * Created by IntelliJ IDEA.
 * User: korawings : Mavel
 * Date: Sun 21 / Sep / 2025
 * Time: 23 : 32
 */
public interface UserService {
  User createUser(User user);
}