package com.comulynx.wallet.rest.api.controller;

import com.comulynx.wallet.rest.api.exception.ResourceNotFoundException;
import com.comulynx.wallet.rest.api.model.Account;
import com.comulynx.wallet.rest.api.model.Customer;
import com.comulynx.wallet.rest.api.repository.AccountRepository;
import com.comulynx.wallet.rest.api.repository.CustomerRepository;
import com.comulynx.wallet.rest.api.util.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(AppUtils.BASE_URL+"/customers")
public class CustomerController {

	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private AccountRepository accountRepository;

	/**
	 * 
	 * Login
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping("/login")
	public ResponseEntity<?> customerLogin(@RequestBody String request) {
		try {

			return ResponseEntity.status(200).body(HttpStatus.OK);

		} catch (Exception ex) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}


	@GetMapping("/")
	public List<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}

	@GetMapping("/{customerId}")
	public ResponseEntity<Customer> getCustomerByCustomerId(@PathVariable(value = "customerId") String customerId)
			throws ResourceNotFoundException {
		Customer customer = customerRepository.findByCustomerId(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found for this id :: " + customerId));
		return ResponseEntity.ok().body(customer);
	}

	@PostMapping("/create")
	public ResponseEntity<?> createCustomer(@RequestBody Customer customer) {
		try {
			// TODO : Add logic to Hash Customer PIN here
			// TODO : Add logic to check if Customer with provided username, or
			// customerId exists. If exists, throw a Customer with [?] exists
			// Exception.

			if (checkUserWithEmail(customer.getEmail())) {
				return new ResponseEntity<>("Webuser with employee email " + customer.getEmail() + " exists", HttpStatus.NOT_FOUND);
			}
			if (checkUserWithCustomerId(customer.getCustomerId())) {
				return new ResponseEntity<>("Webuser with employee customer id " + customer.getCustomerId() + " exists", HttpStatus.NOT_FOUND);
			}



			String accountNo = generateAccountNo(customer.getCustomerId());
			Account account = new Account();
			account.setCustomerId(customer.getCustomerId());
			account.setAccountNo(accountNo);
			account.setBalance(0.0);
			accountRepository.save(account);

			return ResponseEntity.ok().body(customerRepository.save(customer));
		} catch (Exception ex) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}




	public boolean checkUserWithCustomerId(String customerId) {
		return customerRepository.findByCustomerId(customerId).isPresent();
	}

	public boolean checkUserWithEmail(String email) {
		return customerRepository.findByEmail(email).isPresent();
	}


	@PutMapping("/{customerId}")
	public ResponseEntity<Customer> updateCustomer(@PathVariable(value = "customerId") String customerId,
			@RequestBody Customer customerDetails) throws ResourceNotFoundException {
		Customer customer = customerRepository.findByCustomerId(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found for this id :: " + customerId));

		customer.setEmail(customerDetails.getEmail());
		customer.setLastName(customerDetails.getLastName());
		customer.setFirstName(customerDetails.getFirstName());
		final Customer updatedCustomer = customerRepository.save(customer);
		return ResponseEntity.ok(updatedCustomer);
	}

	@DeleteMapping("/{customerId}")
	public Map<String, Boolean> deleteCustomer(@PathVariable(value = "customerId") String customerId)
			throws ResourceNotFoundException {
		Customer customer = customerRepository.findByCustomerId(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found for this id :: " + customerId));

		customerRepository.delete(customer);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

	/**
	 * generate a random but unique Account No (NB: Account No should be unique
	 * in your accounts table)
	 * 
	 */
	private String generateAccountNo(String customerId) {
		// TODO : Add logic here - generate a random but unique Account No (NB:
		// Account No should be unique in the accounts table)
		return "";
	}
}
