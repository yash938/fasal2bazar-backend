package com.agriRoot.repository;

import com.agriRoot.dto.UserDto;
import com.agriRoot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String email);
    public List<User> findByFullNameContaining(String keyword);
}
