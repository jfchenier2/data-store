package ca.gc.triagency.datastore.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Value("${ldap.urls}")
	private String ldapUrls;

	@Value("${ldap.base.dn}")
	private String ldapBaseDn;

	@Value("${ldap.username}")
	private String ldapSecurityPrincipal;

	@Value("${ldap.password}")
	private String ldapPrincipalPassword;

	@Value("${ldap.user.dn.pattern}")
	private String ldapUserDnPattern;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/", "/home", "/webjars/**", "/css/**", "/images/**", "/js/**").permitAll()
				.antMatchers("/datasets/**").hasRole("ADMIN").antMatchers("/entities/**", "/api/**", "/reports/**")
				.hasRole("AGENCY_USER").anyRequest().authenticated().and().formLogin().loginPage("/login").permitAll()
				.and().logout().permitAll().and().exceptionHandling().accessDeniedPage("/exception/forbiden-by-role");
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.ldapAuthentication().userDnPatterns("CN={0},CN=Users").contextSource().url(ldapUrls + ldapBaseDn)
				.managerDn(ldapSecurityPrincipal).managerPassword(ldapPrincipalPassword);
		// .and().passwordCompare()
		// .passwordEncoder(new
		// LdapShaPasswordEncoder()).passwordAttribute("userPassword");
	}
	//
	// @Bean
	// @Override
	// public UserDetailsService userDetailsService() {
	// Collection<UserDetails> users = new ArrayList<UserDetails>();
	// users.add(User.withDefaultPasswordEncoder().username("sshrc-user").password("password")
	// .roles("AGENCY_USER", "SSHRC").build());
	// users.add(User.withDefaultPasswordEncoder().username("nserc-user").password("password")
	// .roles("AGENCY_USER", "NSERC").build());
	// users.add(User.withDefaultPasswordEncoder().username("sshrc-user-edi").password("password")
	// .roles("AGENCY_USER", "SSHRC", "EDI_AUTHORIZED").build());
	// users.add(User.withDefaultPasswordEncoder().username("nserc-user-edi").password("password")
	// .roles("AGENCY_USER", "NSERC", "EDI_AUTHORIZED").build());
	// users.add(User.withDefaultPasswordEncoder().username("admin").password("password").roles("ADMIN",
	// "AGENCY_USER")
	// .build());
	//
	// return new InMemoryUserDetailsManager(users);
	// }
	// }
}
