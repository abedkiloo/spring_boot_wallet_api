package com.comulynx.wallet.rest.api.controller;

import com.comulynx.wallet.rest.api.exception.ResourceNotFoundException;
import com.comulynx.wallet.rest.api.model.Webuser;
import com.comulynx.wallet.rest.api.repository.WebuserRepository;
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
@RequestMapping(AppUtils.BASE_URL + "/webusers")
public class WebuserController {

    @Autowired
    private WebuserRepository webuserRepository;

    @GetMapping("/")
    public List<Webuser> getAllWebusers() {
        return webuserRepository.findAll();
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<Webuser> getWebuserByEmployeeId(@PathVariable(value = "employeeId") String employeeId)
            throws ResourceNotFoundException {
        Webuser webuser = webuserRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Webuser not found for this id :: " + employeeId));
        return ResponseEntity.ok().body(webuser);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createWebuser(@RequestBody Webuser webuser) {

        try {
            // TODO : Add logic to check if Webuser with provided username, or
            // email, or employeeId, or customerId exists.
            // If exists, throw a Webuser with [?] exists Exception.

            if (!checkUserWithUserNameId(webuser.getUsername())) {
                return new ResponseEntity<>("Webuser with employee user name " + webuser.getUsername() + " exists", HttpStatus.NOT_FOUND);
            }
            if (checkUserWithEmail(webuser.getEmail())) {
                return new ResponseEntity<>("Webuser with employee email " + webuser.getEmail() + " exists", HttpStatus.NOT_FOUND);
            }
            if (checkUserWithEmployeeId(webuser.getEmployeeId())) {
                return new ResponseEntity<>("Webuser with employee employee id " + webuser.getEmployeeId() + " exists", HttpStatus.NOT_FOUND);
            }
            if (checkUserWithCustomerId(webuser.getCustomerId())) {
                return new ResponseEntity<>("Webuser with employee customer id " + webuser.getCustomerId() + " exists", HttpStatus.NOT_FOUND);
            }
//            webuserRepository.findByEmail(webuser.getEmail())
//                    .orElseThrow(() -> new ResourceNotFoundException("Webuser with employee email " + webuser.getEmail() + " exists"));
//            return ResponseEntity.ok().body(checkUserWithUserNameId(webuser.getUsername()));

            return ResponseEntity.ok().body(webuserRepository.save(webuser));
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    public boolean checkUserWithUserNameId(String userName) {
        return webuserRepository.findByUsername(userName) == null;
    }

    public boolean checkUserWithEmployeeId(String employeeId) {
        return webuserRepository.findByEmployeeId(employeeId).isPresent();
    }

    public boolean checkUserWithCustomerId(String customerId) {
        return webuserRepository.findByCustomerId(customerId).isPresent();
    }

    public boolean checkUserWithEmail(String email) {
        return webuserRepository.findByEmail(email).isPresent();
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<Webuser> updateWebuser(@PathVariable(value = "employeeId") String employeeId,
                                                 @RequestBody Webuser webuserDetails) throws ResourceNotFoundException {
        Webuser webuser = webuserRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Webuser not found for this id :: " + employeeId));

        webuser.setEmail(webuserDetails.getEmail());
        webuser.setLastName(webuserDetails.getLastName());
        webuser.setFirstName(webuserDetails.getFirstName());
        final Webuser updatedWebuser = webuserRepository.save(webuser);
        return ResponseEntity.ok(updatedWebuser);
    }

    @DeleteMapping("/{employeeId}")
    public Map<String, Boolean> deleteWebuser(@PathVariable(value = "employeeId") String employeeId)
            throws ResourceNotFoundException {
        Webuser webuser = webuserRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Webuser not found for this id :: " + employeeId));

        webuserRepository.delete(webuser);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
