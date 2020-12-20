package com.ws.sep.bankservice.repositories;

import java.util.Optional;


import com.ws.sep.bankservice.models.Merchant;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMerchantRepository extends JpaRepository< Merchant, Long >
{

    Optional< Merchant > findByMerchantId( String id );

}
