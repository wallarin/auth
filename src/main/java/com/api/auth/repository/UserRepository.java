package com.api.auth.repository;

import com.api.auth.DTO.User;
import com.api.auth.DTO.UserInfoResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUserId(String paramString);

    boolean existsByNickname(String paramString);

    boolean existsByPhone(String paramString);

    Optional<User> findByUserId(String paramString);

    @Query("SELECT u.nickname FROM User u WHERE u.userId = :userId")
    String findNicknameByUserId(@Param("userId") String userId);

    @Query("SELECT new com.api.auth.DTO.UserInfoResponse(u.userId, u.nickname, u.phone, u.gender, u.birthdate) " +
            "FROM User u WHERE u.userId = :userId")
    Optional<UserInfoResponse> findUserInfoByUserId(@Param("userId") String userId);

    Optional<User> findByPhone(String phoneNumber);

    Optional<User> findByUserIdAndPhone(String userId, String phone);

}
