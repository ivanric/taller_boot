//package Service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.web.csrf.CsrfFilter;
//import org.springframework.security.web.savedrequest.NullRequestCache;
//
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//	// @formatter:off
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http
//		/*
//		.authorizeRequests()
//			.anyRequest().authenticated()
//			.and()
//		.requestCache()
//			.requestCache(new NullRequestCache())
//			.and()
//		.httpBasic();
//		*/
//		.csrf().disable()
//		.authorizeRequests()
////		.antMatchers("/demo/*").permitAll()
//		.antMatchers("/**").permitAll();
////		.antMatchers("/**").hasRole("USER");
////		.and()
////		.formLogin();
//	}
//	// @formatter:on
//
//	// @formatter:off
//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		auth
//			.inMemoryAuthentication()
//				.withUser("user").password("password").roles("USER");
//	}
//	// @formatter:on
//}