package me.whiteship.accounts;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AccountController {
	
	@Autowired
	private AccountService service;
	
	@Autowired
	private AccountRepository repository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@RequestMapping(value = "/accounts", method = RequestMethod.POST)
	public ResponseEntity createAccount(@RequestBody @Valid AccountDto.Create create, BindingResult result) {
		// Reason why use Dto(AccountDto.Create) here instead of Domain object(Account) is 
		// to clarify which fields of domain object are being used here
		if (result.hasErrors()) {
			// TODO Add Error Response body
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
 		
		Account newAccount = service.createAccount(create);
//		return new ResponseEntity<>(newAccount, HttpStatus.CREATED);
		return new ResponseEntity<>(modelMapper.map(newAccount, AccountDto.Response.class), HttpStatus.CREATED);
	}
	
	@RequestMapping("/hello")
	public @ResponseBody String hello() {
		return "Hello Spring Boot";
	}
}
