//package com.smart.config;
//
//import org.springframework.context.annotation.Bean;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableMethodSecurity
//public class SecurityConfig {
//	
//	@Bean
//	public PasswordEncoder passwordEncoder()
//	{
//		return new BCryptPasswordEncoder();
//	}
//	
//	@Bean
//	public UserDetailsService userDetailsService()
//	{
////		UserDetails normalUser = User
////				.withUsername("prashant")
////				.password(passwordEncoder().encode("123"))
////				.roles("NORMAL")
////				.build();
////		
////		UserDetails adminUser = User
////				.withUsername("admin")
////				.password(passwordEncoder().encode("admin"))
////				.roles("ADMIN")
////				.build();
////		
//////		UserDetails publicUser = User
//////				.withUsername("public")
//////				.password("public")
//////				.roles("ROLE_PUBLIC")
//////				.build();
////		
////		return new InMemoryUserDetailsManager(normalUser, adminUser);
//		return new CustomUserDetailService();
//		
//		
//	}
//	
//	
//	@Bean
//	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
////		httpSecurity.csrf().disable()
////		.authorizeHttpRequests()
////		
//////		.requestMatchers("/home/admin")
//////		.hasRole("ADMIN")
//////		.requestMatchers("/home/normal")
//////		.hasRole("NORMAL")
//////		.requestMatchers("/home/public")
//////		.permitAll()
////		
////		.anyRequest()
////		.authenticated()
////		.and()
////		.formLogin();
//		
//		httpSecurity.authorizeHttpRequests()
//		.requestMatchers("/admin/**")
//		.hasRole("ADMIN")
//		.requestMatchers("/user/**")
//		.hasRole("USER")
//		.requestMatchers("/**")
//		.permitAll().and().formLogin().and().csrf().disable();
//		
//		return httpSecurity.build();
//	}
//	
//
//	
//	
//}
