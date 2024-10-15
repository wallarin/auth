package com.api.auth;

import com.api.auth.DTO.User;
import com.api.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void insertTestUser() {
        User user = new User();
        user.setUserId("test");
        user.setPassword("test1234");
        user.setNickname("한글");
        user.setPhone("01012341234");
        user.setIsVerified("Y");
        user.setGender("M");
        user.setBirthdate(LocalDate.of(1995, 3, 22));

        userRepository.save(user);
    }

    @BeforeEach
    void setup() {
        userRepository.deleteAll();  // 기존 데이터를 삭제하여 테스트 환경을 초기화
        User user = new User();
        user.setUserId("test");
        user.setPassword("test1234");
        user.setNickname("한글");
        user.setPhone("01012341234");
        user.setIsVerified("Y");
        user.setGender("M");
        user.setBirthdate(LocalDate.of(1995, 3, 22));

        userRepository.save(user);
    }

    @Test
    void findAllTest() {
        List<User> userList = userRepository.findAll();

        // Then: userList가 비어 있지 않음을 확인하고, 사용자 데이터 검증
        assertThat(userList).isNotEmpty();  // 데이터가 하나 이상 있는지 확인
        assertThat(userList.size()).isEqualTo(1);  // 저장된 사용자가 1명임을 확인
        User user = userList.get(0);  // 첫 번째 사용자 데이터 가져오기

        // 저장된 데이터의 세부 사항을 검증
        assertThat(user.getUserId()).isEqualTo("test");
        assertThat(user.getNickname()).isEqualTo("한글");
        assertThat(user.getPhone()).isEqualTo("01012341234");
        assertThat(user.getIsVerified()).isEqualTo("Y");
        assertThat(user.getGender()).isEqualTo("M");
        assertThat(user.getBirthdate()).isEqualTo("1995-03-22");
    }
}
