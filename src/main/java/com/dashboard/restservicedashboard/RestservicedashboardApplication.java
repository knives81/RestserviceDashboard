package com.dashboard.restservicedashboard;

import com.dashboard.restservicedashboard.admin.Account;
import com.dashboard.restservicedashboard.admin.AccountRepository;
import com.dashboard.restservicedashboard.info.Infos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableMongoRepositories(basePackages="com.dashboard")
public class RestservicedashboardApplication {

	private final String USERNAME_DEFAULT = "mpinzi";
	private final String PASSWORD_DEFAULT = "pa33w0rd";

	public static void main(String[] args) {
		SpringApplication.run(RestservicedashboardApplication.class, args);
	}

	@Bean
	CommandLineRunner init(final AccountRepository accountRepository, final PasswordEncoder passwordEncoder) {
		return new CommandLineRunner() {
			@Override
			public void run(String... arg0) throws Exception {
				Account account = accountRepository.findByUsername(USERNAME_DEFAULT);
				if (account == null) {
					accountRepository.save(new Account(USERNAME_DEFAULT,passwordEncoder.encode(PASSWORD_DEFAULT)));
				}

			}
		};
	}
}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	AccountRepository accountRepository;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService()).passwordEncoder(new BCryptPasswordEncoder());
	}

	@Bean
	public Infos Infos() {
		return new Infos();
	}

	@Bean
	UserDetailsService userDetailsService() {
		return new UserDetailsService() {

			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				Account account = accountRepository.findByUsername(username);
				if (account != null) {
					return new User(account.getUsername(), account.getPassword(), true, true, true, true,
							AuthorityUtils.createAuthorityList("USER"));
				} else {
					throw new UsernameNotFoundException("could not find the user '" + username + "'");
				}
			}

		};

	}
}

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll().anyRequest().fullyAuthenticated()
				.and().httpBasic().and().authorizeRequests().and().csrf().disable();
	}

}