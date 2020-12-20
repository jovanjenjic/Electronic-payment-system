package com.ws.sep.pcc.repositories;

import java.util.Optional;


import com.ws.sep.pcc.models.BankInfo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBankInfoRepository extends JpaRepository< BankInfo, Long >
{

    Optional< BankInfo > findByPanBankId( String panBankId );

}
