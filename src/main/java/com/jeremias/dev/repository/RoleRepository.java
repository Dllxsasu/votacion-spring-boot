package com.jeremias.dev.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jeremias.dev.models.Role;
import com.jeremias.dev.utils.enums.RoleName;

public interface RoleRepository extends JpaRepository<Role, Long>{
	  Optional<Role> findByName(RoleName roleName);
}
