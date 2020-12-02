package com.ws.sep.seller.repositories;

import java.util.Optional;


import com.ws.sep.seller.models.Role;
import com.ws.sep.seller.models.RoleName;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleRepository extends JpaRepository< Role, Long >
{

    Optional< Role > findByName( RoleName roleName );

}
