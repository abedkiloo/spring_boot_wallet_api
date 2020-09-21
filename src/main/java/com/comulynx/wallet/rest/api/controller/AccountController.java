package com.comulynx.wallet.rest.api.controller;

import com.comulynx.wallet.rest.api.exception.ResourceNotFoundException;
import com.comulynx.wallet.rest.api.model.Account;
import com.comulynx.wallet.rest.api.repository.AccountRepository;
import com.comulynx.wallet.rest.api.util.AppUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(AppUtils.BASE_URL + "/accounts")
public class AccountController {
    private Gson gson = new Gson();

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/")
    public List<Account> getAllAccount() {
        return accountRepository.findAll();
    }

    @GetMapping("/{searchId}")
    public ResponseEntity<?> getAccountByCustomerIdOrAccountNo(
            @PathVariable(value = "searchId") String customerIdOrAccountNo) throws ResourceNotFoundException {
        Account account = accountRepository
                .findAccountByCustomerIdOrAccountNo(customerIdOrAccountNo, customerIdOrAccountNo)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Account not found for this searchId :: " + customerIdOrAccountNo));

        return ResponseEntity.ok().body(account);
    }

    @GetMapping("/balance")
    public ResponseEntity<?> getAccountBalanceByCustomerIdAndAccountNo(@RequestBody String request)
            throws ResourceNotFoundException {

        try {
            JsonObject response = new JsonObject();
//
            final JsonObject balanceRequest = gson.fromJson(request, JsonObject.class);
            String customerId = balanceRequest.get("customerId").getAsString();
            String accountNo = balanceRequest.get("accountNo").getAsString();

//            //  logic to find account balance by CustomerId And
//            // AccountNo
            Account account = accountRepository.findAccountByCustomerIdAndAccountNo(
                    customerId, accountNo
            ).orElseThrow(() -> new ResourceNotFoundException(
                    "Account not found for this customer Id ::" + customerId + " or account no  :: " + accountNo + " not found"));
//
            response.addProperty("balance", account.getBalance());



            return ResponseEntity.ok().body(gson.toJson(response));
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @PostMapping("/")
    public Account createAccount(@RequestBody Account account) {
        return accountRepository.save(account);
    }

}
