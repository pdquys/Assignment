package com.test.fr_ks_java_springboot_p_l001.repository;

import com.test.fr_ks_java_springboot_p_l001.entity.Role;
import com.test.fr_ks_java_springboot_p_l001.entity.RoleEnum;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface RoleRepository extends JpaRepositoryImplementation<Role, UUID> {
    List<Role> findByIdIn(Set<UUID> ids);
    Optional<Role> findByName(RoleEnum name);

}
