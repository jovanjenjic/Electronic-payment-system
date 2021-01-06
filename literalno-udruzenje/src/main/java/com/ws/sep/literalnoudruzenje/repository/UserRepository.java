package com.ws.sep.literalnoudruzenje.repository;

import com.ws.sep.literalnoudruzenje.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional< User > findByEmail(String email );

    Boolean existsByEmail( String email );

    Boolean existsByPib( Long pib );

}
