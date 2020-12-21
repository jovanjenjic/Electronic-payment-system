package com.ws.sep.issuer.repositories;

import java.util.Optional;


import com.ws.sep.issuer.models.Client;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IClientRepository extends JpaRepository< Client, Long >
{

    Optional< Client > findByPan( String pan );

}
