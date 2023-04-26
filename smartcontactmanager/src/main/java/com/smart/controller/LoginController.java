//package com.smart.controller;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//
//@RestController
//@RequestMapping("/home/")
//public class LoginController {
//
//	@GetMapping("/normal")
//	public ResponseEntity<String> normalUser()
//	{
//		return ResponseEntity.ok("Yes, iam normal user");
//	}
//	
//	@PreAuthorize("hasRole('ADMIN')")
//	@GetMapping("/admin")
//	public ResponseEntity<String> adminUser()
//	{
//		return ResponseEntity.ok("Yes, iam admin user");
//	}
//	
//	@GetMapping("/public")
//	public ResponseEntity<String> publicUser()
//	{
//		return ResponseEntity.ok("Yes, iam public user");
//	}
//	
//}
//
