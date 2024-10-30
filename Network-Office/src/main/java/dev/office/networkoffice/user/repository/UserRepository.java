package dev.office.networkoffice.user.repository;

import dev.office.networkoffice.user.entity.SocialType;
import dev.office.networkoffice.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
            SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END
            FROM User u
            WHERE u.oAuthInfo.socialId = :socialId AND u.oAuthInfo.socialType = :socialType""")
    boolean existsBySocialLogin(@Param("socialId") String socialId, @Param("socialType") SocialType socialType);

    @Query("""
            SELECT u
            FROM User u
            WHERE u.oAuthInfo.socialId = :socialId AND u.oAuthInfo.socialType = :socialType""")
    Optional<User> findBySocialLogin(@Param("socialId") String socialId, @Param("socialType") SocialType socialType);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByProfileDisplayName(String displayName);
}
