package com.jeon.auth_api.app.domain.user.repository;

import com.jeon.auth_api.app.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
