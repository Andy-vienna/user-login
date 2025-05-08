package com.example.demo.repository;

import com.example.demo.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByUsername_returnsEmpty() {
        assertThat(userRepository.findByUsername("nonexistent")).isEmpty();
    }

    @Test
    void testFindByEmail_returnsEmpty() {
        assertThat(userRepository.findByEmail("none@example.com")).isEmpty();
    }
}