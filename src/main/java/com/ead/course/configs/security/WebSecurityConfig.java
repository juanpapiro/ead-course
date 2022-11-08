package com.ead.course.configs.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private AuthenticationEntryPoint authenticationEntryPoint;
	
	@Bean
	public AuthenticationJwtFilter authenticationJwtFilter() {
		return new AuthenticationJwtFilter();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests()
			.anyRequest().authenticated()
			.and()
			.csrf().disable();
		http.addFilterBefore(authenticationJwtFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean
	public RoleHierarchy roleHierarchy() {
		RoleHierarchyImpl roleHierarchyImpl = new RoleHierarchyImpl();
		StringBuilder sb = new StringBuilder();
		sb.append("ROLE_ADMIN")
		  .append(" > ")
		  .append("ROLE_INSTRUCTOR")
		  .append(" \n")
		  .append("ROLE_INSTRUCTOR")
		  .append(" > ")
		  .append("ROLE_STUDENT")
		  .append(" \n")
		  .append("ROLE_STUDENT")
		  .append(" > ")
		  .append("ROLE_USER");
		roleHierarchyImpl.setHierarchy(sb.toString());
		return roleHierarchyImpl;
	}
}
