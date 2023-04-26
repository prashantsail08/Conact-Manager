package com.smart.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.config.helper.Message;
import com.smart.dao.UserRepository;

import com.smart.entities.User;

@Controller
public class HomeController {

	@Autowired // new added 1
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/")
	public String home(Model m) {
		m.addAttribute("title", "Home -  Smart Contact Manager");

		return "home";
	}

	@GetMapping("/about")
	public String about(Model m) {
		m.addAttribute("title", "About -  Smart Contact Manager");

		return "about";
	}

	@GetMapping("/signup")
	public String signup(Model m) {
		m.addAttribute("title", "Register -  Smart Contact Manager");
		m.addAttribute("user", new User());

		return "signup";
	}

	// this handler for registering user

	@RequestMapping(value = "/do_register", method = RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model m,
			HttpSession session) {
		try {
			System.out.println("registerUser : ");
			
			if (!agreement) {
				System.out.println("You not agreed the terms and conditions");
				throw new Exception("You not agreed the terms and conditions");
			}

			if (bindingResult.hasErrors()) {
				System.out.println("ERROR" + bindingResult.toString());
				m.addAttribute("user", user);
				return "signup";
			}

			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			// new added 2
			user.setPassword(passwordEncoder.encode(user.getPassword()));

			System.out.println("agreement" + agreement);
			System.out.println("USER" + user);

			User result = this.userRepository.save(user);

			m.addAttribute("user", new User());
			session.setAttribute("message", new Message("Successfully Registered !!", "alert-success"));
			return "signup";
			
			

		} catch (Exception e) {
			System.out.println("registerUser : ");
			
			e.printStackTrace();
			m.addAttribute("user", user);
			session.setAttribute("message", new Message("something Went Worng !! " + e.getMessage(), "alert-danger"));
			return "signup";
		}
		

	}

	// handler for custom login
	@RequestMapping("/signin")
	public String customLogin(Model model) {
		
		System.out.println("customLogin : ");
		
		model.addAttribute("title", "Login Page");
		
		return "login";
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
