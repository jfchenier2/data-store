package ca.gc.triagency.datastore.app.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;

import ca.gc.triagency.datastore.security.CustomAuthoritiesMapper;

@EnableWebSecurity
public class WebSecurityConfig {
	@Value("${ldap.url.nserc}")
	private String ldapUrlNSERC;

	@Value("${ldap.base.dn.nserc}")
	private String ldapBaseDnNSERC;

	@Value("${ldap.domain.nserc}")
	private String ldapDomainNSERC;
	
	@Value("${ldap.url.sshrc}")
	private String ldapUrlSSHRC;

	@Value("${ldap.base.dn.sshrc}")
	private String ldapBaseDnSSHRC;

	@Value("${ldap.domain.sshrc}")
	private String ldapDomainSSHRC;
	
	@Value("${use.active.directory}")
	private String useActiveDirectory;
	
	@Value("${ldap.group.search.base}")
	private String ldapGroupSearchBase;

	@Value("${ldap.user.dn.pattern}")
	private String ldapUserDnPattern;
	
	@Value("${ldap.base.dn}")
	private String ldapBaseDn;
	
	@Value("${ldap.urls}")
	private String ldapUrls;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		if(Boolean.parseBoolean(useActiveDirectory)) {
			auth.authenticationProvider(activeDirectoryLdapAuthenticationProviderNSERC());
			auth.authenticationProvider(activeDirectoryLdapAuthenticationProviderSSHRC());
		}else {
			auth.ldapAuthentication().userDnPatterns(ldapUserDnPattern).groupSearchBase(ldapGroupSearchBase).contextSource()
				.url(ldapUrls + ldapBaseDn).and().passwordCompare().passwordAttribute("userPassword");
		}
			
	}

	@Configuration
	@Order(1)
	public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
		protected void configure(HttpSecurity http) throws Exception {
			http.antMatcher("/api/**").authorizeRequests().anyRequest().hasAnyRole("NSERC_USER", "SSHRC_USER", "AGENCY_USER").anyRequest()
					.authenticated().and().httpBasic();
		}
	}

	@Configuration
	@Order(2)
	public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests().antMatchers("/", "/home", "/webjars/**", "/css/**", "/images/**", "/js/**").permitAll()
					.and().authorizeRequests().antMatchers("/datasets/**").hasRole("ADMIN")
					.antMatchers("/entities/**", "/reports/**").hasAnyRole("NSERC_USER", "SSHRC_USER", "AGENCY_USER").anyRequest().authenticated()
					.and().formLogin().loginPage("/login").permitAll().and().logout().permitAll()
					.and().exceptionHandling().accessDeniedPage("/exception/forbiden-by-role");
		}
	}
    
	@Bean
    public ProviderManager authenticationManager() {
        return new ProviderManager(Arrays.asList(activeDirectoryLdapAuthenticationProviderNSERC(), activeDirectoryLdapAuthenticationProviderSSHRC()));
    }
	
    @Bean
    public AuthenticationProvider activeDirectoryLdapAuthenticationProviderNSERC() {
        ActiveDirectoryLdapAuthenticationProvider nsercProvider = new ActiveDirectoryLdapAuthenticationProvider(ldapDomainNSERC, ldapUrlNSERC, ldapBaseDnNSERC);
        CustomAuthoritiesMapper authMapper = new CustomAuthoritiesMapper();
        authMapper.setDefaultAuthority("NSERC_USER");
        nsercProvider.setConvertSubErrorCodesToExceptions(true);
        nsercProvider.setUseAuthenticationRequestCredentials(true);
        nsercProvider.setAuthoritiesMapper(authMapper);
       
        return nsercProvider;
    }
	
    @Bean
    public AuthenticationProvider activeDirectoryLdapAuthenticationProviderSSHRC() {
        ActiveDirectoryLdapAuthenticationProvider sshrcProvider = new ActiveDirectoryLdapAuthenticationProvider(ldapDomainSSHRC, ldapUrlSSHRC, ldapBaseDnSSHRC);
        CustomAuthoritiesMapper authMapper = new CustomAuthoritiesMapper();
        authMapper.setDefaultAuthority("SSHRC_USER");
        sshrcProvider.setConvertSubErrorCodesToExceptions(true);
        sshrcProvider.setUseAuthenticationRequestCredentials(true);
        sshrcProvider.setAuthoritiesMapper(authMapper);
        
        return sshrcProvider;
    }
	
	
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
	// LdapTemplate getLdapTemplate(ContextSource contextSource) {
	// LdapTemplate retval = new LdapTemplate(contextSource);
	// retval.setIgnorePartialResultException(true);
	// return retval;
	// }

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
}