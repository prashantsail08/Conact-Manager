package com.smart.controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.service.EmailService;

@Controller
public class ForgetController {

	Random random = new Random(1000);
	
	@Autowired
	private EmailService emailService;

	// email id form open handler
	@RequestMapping("/forgot")
	public String openEmailForm()
	{
		return "forgot_email_form";
	}
	
	@RequestMapping("/send-otp")
	public String sendOtp(@RequestParam("email") String email, HttpSession session)
	{
		System.out.println("EMAIL : "+ email);
		
		//generating otp of 4 digit
		
		int otp = random.nextInt(999999);
		
		System.out.println("OTP : "+otp);
		
		//write code to send otp to email
		
		String subject = "OTP from SCM";
		String messaage = "<h1> OTP = "+otp+" </h1>";
		String to= email;
		
		boolean flag = this.emailService.sendEmail(subject, messaage, to);
	
		if(flag)
		{
			
			return "verify_otp";
			
		}else {
			
			session.setAttribute("messaage", "check your email id !!");
			
			return  "forgot_email_form";
		}
		
	}
}
