package com.luwings.springsecurityhttpbasicauth.service;

import com.luwings.springsecurityhttpbasicauth.dto.AuthorityDTO;
import com.luwings.springsecurityhttpbasicauth.dto.RoleDTO;
import com.luwings.springsecurityhttpbasicauth.dto.UserDTO;
import com.luwings.springsecurityhttpbasicauth.entity.AuthorityEntity;
import com.luwings.springsecurityhttpbasicauth.entity.RoleEntity;
import com.luwings.springsecurityhttpbasicauth.entity.UserEntity;
import com.luwings.springsecurityhttpbasicauth.model.AuthenticatedUser;
import com.luwings.springsecurityhttpbasicauth.repository.AuthorityRepository;
import com.luwings.springsecurityhttpbasicauth.repository.RolesRepository;
import com.luwings.springsecurityhttpbasicauth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserEntity register(UserDTO userDTO) {
        System.out.println("Request body: " + userDTO);

        UserEntity u = new UserEntity();
        u.setEmail(userDTO.getEmail());
        u.setUsername(userDTO.getUsername());
        u.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        Set<RoleEntity> roleEntities = userDTO.getRoles().stream().map(roleDTO -> {
            //Single role
            RoleEntity roleEntity = getOrCreateRole(roleDTO);

            //multiple authorities
            Set<AuthorityEntity> authorities = getAuthoritiesFromRequest(roleDTO);

            roleEntity.setAuthorities(authorities);

            return roleEntity;
        }).collect(Collectors.toSet());

        u.setRoleEntities(roleEntities);

        return userRepository.save(u);
    }

    private Set<AuthorityEntity> getAuthoritiesFromRequest(RoleDTO roleDTO) {
        return roleDTO.getAuthorities().stream().map(authorityDTO -> {
            return getOrCreateAuthority(authorityDTO);
        }).collect(Collectors.toSet());
    }

    private AuthorityEntity getOrCreateAuthority(AuthorityDTO authorityDTO) {
        return authorityRepository.findByName(authorityDTO.getName()).orElseGet(() -> {
            AuthorityEntity auth = new AuthorityEntity();
            auth.setName(authorityDTO.getName());
            return authorityRepository.save(auth);
        });
    }

    private RoleEntity getOrCreateRole(RoleDTO roleDTO) {
        return rolesRepository.findByName(roleDTO.getName()).orElseGet(() -> {
            RoleEntity r = new RoleEntity();
            r.setName(roleDTO.getName());
            return rolesRepository.save(r);
        });
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity u = userRepository.findByEmail(username);

        //fetch the authorities
        List<GrantedAuthority> authorities = u.getRoleEntities().stream()
                .flatMap(roleEntity -> roleEntity.getAuthorities()
                    .stream()
                    .map(authorityEntity -> new SimpleGrantedAuthority(roleEntity.getName()+"_"+ authorityEntity.getName())))
            .collect(Collectors.toList());

        System.out.println("authorities:::::::::" + authorities);

        //fetch the role
        authorities.addAll(u.getRoleEntities()
            .stream()
            .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName()))
            .toList());

        System.out.println("Role:::::::::" + authorities);

        return new AuthenticatedUser(u, authorities);
    }
}