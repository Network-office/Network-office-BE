package dev.office.networkoffice.user.repository;

import dev.office.networkoffice.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
