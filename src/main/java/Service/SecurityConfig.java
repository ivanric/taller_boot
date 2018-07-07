//package Service;
//
//import javax.sql.DataSource;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.web.csrf.CsrfFilter;
//import org.springframework.security.web.savedrequest.NullRequestCache;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//	@Autowired
//	DataSource dataSource;
//
//	@Autowired
//	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
//		auth.jdbcAuthentication().dataSource(dataSource)
//				.usersByUsernameQuery("select login,password from usuario where login=?")
//				.authoritiesByUsernameQuery("select u.login,r.nombre from rol r,usuario u,rolusu  rs where r.idrol=rs.idrol and u.login=rs.login and u.login=?");
//	}
//	
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.authorizeRequests()
////			.antMatchers("/principal/index").permitAll()
////			.antMatchers("/RestUsuario"+"/Jwt").hasRole("administrador")
////			.anyRequest().authenticated()
////			.and().formLogin().loginPage("/principal"+"/index2").permitAll()
////			.and().logout()
////			.permitAll();
////		http.exceptionHandling().accessDeniedPage("/403");
//		.antMatchers("/**").permitAll();
//	
//	}
//}