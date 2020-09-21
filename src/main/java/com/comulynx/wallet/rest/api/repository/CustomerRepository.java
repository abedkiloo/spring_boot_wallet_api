package com.comulynx.wallet.rest.api.repository;

import com.comulynx.wallet.rest.api.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByCustomerId(String customerId);


    Optional<Customer> findByEmail(String employeeEmail);


    // Implement the query and function below to delete a customer using Customer Id
    @Query("DELETE FROM Customer c where c.customerId=:customer_id")
    int deleteCustomerByCustomerId(String customer_id);

    //  Implement the query and function below to update customer firstName using Customer Id
    @Query("update Customer c set c.firstName=:firstName where c.customerId=:customer_id")
    int updateCustomerByCustomerId(String firstName, String customer_id);

    //  Implement the query and function below and to return all customers whose Email contains  'gmail'
     @Query("SELECT C FROM Customer c where c.email like '%gmail%'")
	 List<Customer> findAllCustomersWhoseEmailContainsGmail();
}
