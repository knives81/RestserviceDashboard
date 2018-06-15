package com.dashboard.restservicedashboard.admin;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
    PasswordEncoder passwordEncoder;
	
	
	private static final Logger log = LoggerFactory.getLogger(AdminController.class);
	
	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public ResponseEntity addAccount(@RequestBody Account account) {
		Account foundAccount = accountRepository.findByUsername(account.getUsername());
		if (foundAccount == null) {				
			accountRepository.save(new Account(account.getUsername(),passwordEncoder.encode(account.getPassword())));
		}
		return new ResponseEntity(HttpStatus.OK);
	}
	

}