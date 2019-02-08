package ca.gc.triagency.datastore.app.config;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter  {
//	@Value("${ldap.urls}")
//	private String ldapUrls;
//
//	@Value("${ldap.base.dn}")
//	private String ldapBaseDn;
//
//	@Value("${ldap.username}")
//	private String ldapSecurityPrincipal;
//
//	@Value("${ldap.password}")
//	private String ldapPrincipalPassword;
//
//	@Value("${ldap.user.dn.pattern}")
//	private String ldapUserDnPattern;

//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.authorizeRequests().antMatchers("/", "/home", "/webjars/**", "/css/**", "/images/**", "/js/**").permitAll()
//				.antMatchers("/datasets/**").hasRole("ADMIN").antMatchers("/entities/**", "/api/**", "/reports/**")
//				.hasRole("AGENCY_USER").anyRequest().authenticated().and().formLogin().loginPage("/login").permitAll()
//				.and().logout().permitAll().and().exceptionHandling().accessDeniedPage("/exception/forbiden-by-role");
//	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/", "/home", "/webjars/**", "/css/**", "/images/**", "/js/**").permitAll()
				.antMatchers("/datasets/**").hasRole("ADMIN").antMatchers("/entities/**", "/api/**", "/reports/**")
				.hasRole("AGENCY_USER").anyRequest().authenticated().and().httpBasic();

	}
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.ldapAuthentication().userDnPatterns("uid={0},ou=people").groupSearchBase("ou=groups").contextSource()
		.url("ldap://localhost:8389/dc=nserc_poc,dc=net").and().passwordCompare()
		.passwordAttribute("userPassword");
	}

//
//	@Configuration
//    @Order(1)
//    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
//
//        @Override
//        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//    		auth.ldapAuthentication().userDnPatterns("uid={0},ou=people").groupSearchBase("ou=groups").contextSource()
//			.url("ldap://localhost:8389/dc=nserc_poc,dc=net").and().passwordCompare()
//			.passwordAttribute("userPassword");
//        }
//
//		@Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http.authorizeRequests()
//                .antMatchers("/api/**").hasRole("AGENCY_USER")
//                .and()
//                .httpBasic();
//        }
//    }
//	
//	@Configuration   
//	@Order(2)
//	public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
//		@Override
//		public void configure(AuthenticationManagerBuilder auth) throws Exception {
//			auth.ldapAuthentication().userDnPatterns("uid={0},ou=people").groupSearchBase("ou=groups").contextSource()
//					.url("ldap://localhost:8389/dc=nserc_poc,dc=net").and().passwordCompare()
//					.passwordAttribute("userPassword");
//		}
//		
//		@Override
//		protected void configure(HttpSecurity http) throws Exception {
//			http.authorizeRequests().antMatchers("/", "/home", "/webjars/**", "/css/**", "/images/**", "/js/**").permitAll()
//					.antMatchers("/datasets/**").hasRole("ADMIN").antMatchers("/entities/**", "/reports/**")
//					.hasRole("AGENCY_USER").anyRequest().authenticated().and().formLogin().loginPage("/login").permitAll()
//					.and().logout(	).permitAll().and().exceptionHandling().accessDeniedPage("/exception/forbiden-by-role");
//		}
//	}
//	

	// @Override
	// public void configure(AuthenticationManagerBuilder auth) throws Exception
	// {
	// LdapContextSource lcs = new LdapContextSource();
	// lcs.setUserDn(ldapSecurityPrincipal);
	// lcs.setPassword(ldapPrincipalPassword);
	// lcs.setUrl(ldapUrls);
	// lcs.setReferral("follow");
	// lcs.setBase(ldapBaseDn);
	// lcs.afterPropertiesSet();
	// auth.ldapAuthentication().userDnPatterns("CN={0},CN=Users").contextSource(lcs);
	//
	// // .url(ldapUrls + ldapBaseDn)
	// //
	// .managerDn(ldapSecurityPrincipal).managerPassword(ldapPrincipalPassword);
	// //
	// .managerDn(ldapSecurityPrincipal).managerPassword(ldapPrincipalPassword);
	// // .and().passwordCompare()
	// // .passwordEncoder(new
	// // LdapShaPasswordEncoder()).passwordAttribute("userPassword");
	// }

	// @Bean
	// ContextSource contextSource(AuthenticationSource authenticationSource) {
	// LdapContextSource retval = new LdapContextSource();
	// retval.setAuthenticationSource(authenticationSource);
	// retval.setUrl("ldap://52.60.217.219:389/");
	// retval.setBase("dc=nserc_poc,dc=net");
	// retval.setUserDn("CN={0},CN=Users");
	// retval.
	// return retval;
	// }

//	@Bean
//	LdapTemplate getLdapTemplate(ContextSource contextSource) {
//		LdapTemplate retval = new LdapTemplate(contextSource);
//		retval.setIgnorePartialResultException(true);
//		return retval;
//	}

	//
//	 @Bean
//	 @Override
//	 public UserDetailsService userDetailsService() {
//		 Collection<UserDetails> users = new ArrayList<UserDetails>();
//		 users.add(User.withDefaultPasswordEncoder().username("sshrc-user").password("password")
//		 .roles("AGENCY_USER", "SSHRC").build());
//		 users.add(User.withDefaultPasswordEncoder().username("nserc-user").password("password")
//		 .roles("AGENCY_USER", "NSERC").build());
//		 users.add(User.withDefaultPasswordEncoder().username("sshrc-user-edi").password("password")
//		 .roles("AGENCY_USER", "SSHRC", "EDI_AUTHORIZED").build());
//		 users.add(User.withDefaultPasswordEncoder().username("nserc-user-edi").password("password")
//		 .roles("AGENCY_USER", "NSERC", "EDI_AUTHORIZED").build());
//		 users.add(User.withDefaultPasswordEncoder().username("admin").password("password").roles("ADMIN",
//		 "AGENCY_USER")
//		 .build());
//		
//		 return new InMemoryUserDetailsManager(users);
//	 }
}
