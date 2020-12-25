package com.ws.sep.acquirer.repositories;

import java.util.Optional;


import com.ws.sep.acquirer.models.Client;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IClientRepository extends JpaRepository< Client, Long >
{

    Boolean existsByPan( String pan );

    Optional< Client > findByMerchantIdAndMerchantPassword( String id, String password );

    Client findByMerchantId( String merchantId );

    Optional< Client > findByPan( String pan );

}
