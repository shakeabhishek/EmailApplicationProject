package com.test.EmailApplicationProject.repositories;

import com.test.EmailApplicationProject.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "UPDATE USER u set u.ID = ?1, u.FIRST_NAME = ?2, u.LAST_NAME = ?3, u.EMAIL = ?4, u.PASSWORD = ?5", nativeQuery = true)
    void updateUser(int id, String firstName, String lastName, String email, String password);
}
