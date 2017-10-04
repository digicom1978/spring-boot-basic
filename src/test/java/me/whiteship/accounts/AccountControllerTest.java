package me.whiteship.accounts;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.whiteship.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
//@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = )
@WebAppConfiguration
@Transactional	// Transactional in Test is doing rollback for all data change
//@Rollback(true)	// Whole test result will be rolled back
public class AccountControllerTest {
	
	@Autowired
	WebApplicationContext wac;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	AccountService service;

	MockMvc mockMvc;
	
	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	@Test
	//@commit	// Method level, individual test result won't be rolled back.
	public void createAccount() throws Exception {
		AccountDto.Create createDto = new AccountDto.Create();
		createDto.setUsername("whiteship");
		createDto.setPassword("password");
		
		ResultActions result = mockMvc.perform(post("/accounts")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(createDto)));
		
		result.andDo(print());
		result.andExpect(status().isCreated());
		
		// {"id":1,"username":"whiteship","fullName":null,"joined":1503376364784,"updated":1503376364784}
	    result.andExpect(jsonPath("$.username", is("whiteship")));
		
		// Check duplicated user create
		result = mockMvc.perform(post("/accounts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createDto)));
			
		result.andDo(print());
		result.andExpect(status().isBadRequest());
		result.andExpect(jsonPath("$.code", is("duplicated.username.exception")));
	}
	
	@Test
	public void createAccount_BadRequest() throws Exception {
		AccountDto.Create createDto = new AccountDto.Create();
		createDto.setUsername(" ");
		createDto.setPassword("1234");
		
		ResultActions result = mockMvc.perform(post("/accounts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createDto)));
		
		result.andDo(print());
		result.andExpect(status().isBadRequest());
		result.andExpect(jsonPath("$.code", is("bad.request")));
	}
	
	
	// TODO getAccounts()
	
	@Test
	public void getAccounts() throws Exception{
		AccountDto.Create createDto = new AccountDto.Create();
		createDto.setUsername("whiteship");
		createDto.setPassword("password");
		service.createAccount(createDto);
		
		ResultActions result = mockMvc.perform(get("/accounts"));
		
		result.andDo(print());
		result.andExpect(status().isOk());
	}
}
