package com.luwings.hexa.architecture.adapter.in.web;

import com.luwings.hexa.architecture.domain.port.in.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 * User: korawings : Mavel
 * Date: Sun 21 / Sep / 2025
 * Time: 23 : 35
 */
@RestController
@RequestMapping("/users")
public class UserController {
  private final UserService userService;
  public UserController(UserService userService) {
    this.userService = userService;
  }
  @PostMapping
  public User createUser(@RequestBody User user) {
    return userService.createUser(user);
  }
}