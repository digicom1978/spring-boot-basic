package me.whiteship.accounts;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import me.whiteship.commons.ErrorResponse;

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
			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setMessage("Bad Request");
			errorResponse.setCode("bad.request");
			// TODO Use error information in BindingResult (BindingResult includes more details about error cause)
			return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
		}
 		
		Account newAccount = service.createAccount(create);
		
		/**
		 * How to verify whether service works as expected
		 * - No exception means service works correctly
		 *  
		 * 1. verify by using return type (whether it is object or null)
		 *    - Bad: need to use "if" to check null and handle error
		 *    ex) if (newAccount == null)
		 *            DO Error Handling
		 * 
		 * 2. Use parameter (varible or object)
		 *    - Send parameter to funcion as function parameter for error verification
		 *    - Check parameter and handle error
		 *    - more intuitive than 1, but still inclear 
		 *    - Bad: need to use if to check null and handle error
		 * 
		 * Good of 1, 2: can generate error response to view side.
		 * 
		 * 3. Throw exception with more details if needed, from where error happens
		 *    - Good: if execution passes that service call at controller, 
		 *            that means there is no error, and service works as expected
		 *            ==> clean code
		 *            
		 *            in order to generate error response, user ExceptionHandler
		 *            
		 * 4. Unsynchronious handle
		 * 
		 * service.createAccount(create)
		 * .onSuccess(account -> 
		 *     new ResponseEntity<>(modelMapper.map(newAccount, AccountDto.Response.class), HttpStatus.CREATED);
		 * ) // if function body is one line, then no need to use {} and return 
		 * .onFailure(e ->
		 *     handleUserDuplicatedException(e);
		 * );
		 */
//		return new ResponseEntity<>(newAccount, HttpStatus.CREATED);
		return new ResponseEntity<>(modelMapper.map(newAccount, AccountDto.Response.class), HttpStatus.CREATED);
	}
	
	// Exception handling using kind of call back
	@ExceptionHandler(UserDuplicatedException.class)
	public ResponseEntity handleUserDuplicatedException(UserDuplicatedException e) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setMessage("["+e.getUsername() + "] is duplicated user");
		errorResponse.setCode("duplicated.username.exception");
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping("/hello")
	public @ResponseBody String hello() {
		return "Hello Spring Boot";
	}
	
	// If you set Spring Data JPA in classpath, then just can use Pageable for paging
	// Otherwise, need to set @EnableSpringDataWebSupport before class
	// or <bean class="org.springframework.data.web.config.SpringDataWebConfiguration" /> in XML
	// For example, if request is /accounts?page=0&size=20&sort=username&sort=joined,desc
	// page related parameters will be assigned to Pageable automatically
	@RequestMapping(value = "/accounts", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity getAccounts(Pageable pageable) {
		Page<Account> page = repository.findAll(pageable);	// If you don't have service logic, then you can use repository directly
		
		// 1. Since Account has password, it's not good to return page immediately
		// 2. in case of using lazy fatching or function used in map() is too simple, don't use parallelStream().
		List<AccountDto.Response> content = page.getContent().parallelStream()
			.map(account -> modelMapper.map(account, AccountDto.Response.class))
			.collect(Collectors.toList());
		
		PageImpl<AccountDto.Response> result = new PageImpl<>(content, pageable, page.getTotalElements());
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
