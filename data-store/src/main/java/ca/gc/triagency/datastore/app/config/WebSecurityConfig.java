package ca.gc.triagency.datastore.app.config;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/", "/home", "/webjars/**", "/css/**", "/images/**", "/js/**").permitAll()
				.antMatchers("/datasets/**").hasRole("ADMIN").antMatchers("/entities/**", "/api/**", "/reports/**")
				.hasRole("AGENCY_USER").anyRequest().authenticated().and().formLogin().loginPage("/login").permitAll()
				.and().logout().permitAll().and().exceptionHandling().accessDeniedPage("/exception/forbiden-by-role");
	}

	@Bean
	@Override
	public UserDetailsService userDetailsService() {
		Collection<UserDetails> users = new ArrayList<UserDetails>();
		users.add(User.withDefaultPasswordEncoder().username("sshrc-user").password("password")
				.roles("AGENCY_USER", "SSHRC").build());
		users.add(User.withDefaultPasswordEncoder().username("nserc-user").password("password")
				.roles("AGENCY_USER", "NSERC").build());
		users.add(User.withDefaultPasswordEncoder().username("sshrc-user-edi").password("password")
				.roles("AGENCY_USER", "SSHRC", "EDI_AUTHORIZED").build());
		users.add(User.withDefaultPasswordEncoder().username("nserc-user-edi").password("password")
				.roles("AGENCY_USER", "NSERC", "EDI_AUTHORIZED").build());
		users.add(User.withDefaultPasswordEncoder().username("admin").password("password").roles("ADMIN", "AGENCY_USER")
				.build());

		return new InMemoryUserDetailsManager(users);
	}
}