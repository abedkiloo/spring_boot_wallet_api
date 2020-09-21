package com.comulynx.wallet.rest.api.repository;

import com.comulynx.wallet.rest.api.model.Webuser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WebuserRepository extends JpaRepository<Webuser, Long> {
    Webuser findByUsername(String username);

    Optional<Webuser> findByEmployeeId(String employeeId);

    Optional<Webuser> findByCustomerId(String customerId);

    Optional<Webuser> findByEmail(String employeeEmail);


}
