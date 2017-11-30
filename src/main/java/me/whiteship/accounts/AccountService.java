package me.whiteship.accounts;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class AccountService {
	
	// with @Slf4j annotation, no need to declare logger manually
	// log varible can replace logger
	// supported by lombok
//	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AccountRepository repository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	public Account createAccount(AccountDto.Create dto) {
		/*
		// Way 1
		Account account = new Account();
		account.setUsername(dto.getUsername());
		account.setPassword(dto.getPassword());*/

		/*
		// Way 2
		Account account = new Account();
		BeanUtils.copyProperties(dto, account);*/
		
		// Way 3
		Account account  = modelMapper.map(dto, Account.class);
		
		// TODO verify username is valid or not
		String username = dto.getUsername();
		if (repository.findByUsername(username) != null) {
//			logger.error("user duplicated exception. {}", username);
			log.error("user duplicated exception. {}", username);
			throw new UserDuplicatedException(username);
		}
		
		/*
		// with slf4j, doesn't need to wrap with if
		// reason why wapping if was getting more optimization on string
		if( logger.isDebugEnabled() ) {
			logger.debug("whitehi" + "username");
		}*/
		
		// TODO hashing password
		
		Date now = new Date();
		account.setJoined(now);
		account.setUpdated(now);
		
		return repository.save(account);
		
	}
	
//	public Account updateAccount(Account account, AccountDto.Update updateDto) {
	public Account updateAccount(Long id, AccountDto.Update updateDto) {
		Account account = getAccount(id);
		account.setPassword(updateDto.getPassword());
		account.setFullName(updateDto.getFullName());
		return repository.save(account);
	}

	public Account getAccount(Long id) {
		Account account = repository.findOne(id);
		if( account == null ) {
			throw new AccountNotFoundException(id);
		}
		return account;
	}
}
