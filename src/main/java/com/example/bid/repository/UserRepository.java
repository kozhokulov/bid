package com.example.bid.repository;

import com.example.bid.domain.Role;
import com.example.bid.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    List<User> getUsersByRoles(Collection<Role> roles);
}
