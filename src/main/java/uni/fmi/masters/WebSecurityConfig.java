package uni.fmi.masters;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@EnableGlobalMethodSecurity(
		prePostEnabled = true,
		securedEnabled = true,
		jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	private ApplicationUserDetailService userDetailService;
	
	public WebSecurityConfig(ApplicationUserDetailService userDetailService) {
		this.userDetailService = userDetailService;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		super.configure(auth);
		auth.userDetailsService(userDetailService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/**").permitAll()
		.requestMatchers(EndpointRequest.toAnyEndpoint())
		.permitAll()
		.and().csrf().disable();
		http.headers().frameOptions().disable();		
	}	
	
	
	
	@Bean
	public UserDetailsService userDetailsService() {		
		
		return userDetailService;	
		
	}

}
