package com.api.auth.repository;

import com.api.auth.DTO.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUserId(String paramString);

    boolean existsByNickname(String paramString);

    boolean existsByPhone(String paramString);

    Optional<User> findByUserId(String paramString);

    @Query("SELECT u.nickname FROM User u WHERE u.userId = :userId")
    String findNicknameByUserId(@Param("userId") String userId);
}
