package com.kamiljach.devjobshub.repository;

import com.kamiljach.devjobshub.TestDataUtil;
import com.kamiljach.devjobshub.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;





    @Test
    void UserRepository_Save_ReturnSavedPokemon() {
        User user = TestDataUtil.createTestUserA();
        User savedUser =  userRepository.save(user);

        Assertions.assertThat(savedUser).isNotNull();
    }
}
