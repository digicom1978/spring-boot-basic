package me.whiteship.accounts;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AccountController {
	@RequestMapping("/hello")
	public @ResponseBody String hello() {
		return "Hello Spring Boot";
	}
}
