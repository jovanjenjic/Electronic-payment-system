package com.ws.sep.seller.repositories;

import java.util.Optional;


import com.ws.sep.seller.models.Seller;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISellerRepository extends JpaRepository< Seller, Long >
{

    Optional< Seller > findByEmail( String email );

}
