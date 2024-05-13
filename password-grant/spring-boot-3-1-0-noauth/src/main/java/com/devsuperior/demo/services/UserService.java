package com.devsuperior.demo.services;

import com.devsuperior.demo.entities.Role;
import com.devsuperior.demo.entities.User;
import com.devsuperior.demo.projections.UserDetailProjection;
import com.devsuperior.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetailProjection> userDetailProjections = userRepository.searchUserAndRolesByEmail(username);

        if(userDetailProjections.isEmpty())
            throw new UsernameNotFoundException("User not found");

        User user = new User();
        user.setEmail(username);
        user.setPassword(userDetailProjections.get(0).getPassword());

        for (UserDetailProjection userDetailProjection : userDetailProjections) {
            user.addRole(new Role(userDetailProjection.getAuthority(), userDetailProjection.getRoleId()));
        }

        return user;
    }
}
