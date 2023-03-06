//package io.marcus.rolebase.config;
//
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//@EnableWebSecurity
//public class ApplicationSecurity extends WebSecurityConfigurerAdapter {
//}

package io.marcus.rolebase.config;


import io.marcus.rolebase.user.UserRepository;
import io.marcus.rolebase.util.JwtTokenFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = false, securedEnabled = false, jsr250Enabled = true)
@Configuration(enforceUniqueMethods = false)
public class ApplicationSecurity  {
    // additional security configurations can be added here
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeHttpRequests().requestMatchers("/auth/login", "/docs/**", "/users").permitAll().anyRequest().authenticated();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> {
                            response.sendError(
                                    HttpServletResponse.SC_UNAUTHORIZED,
                            ex.getMessage());
                        }
                );
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

//    @Bean
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(
//                username -> userRepo.findByEmail(username)
//                        .orElseThrow(
//                                () -> new RuntimeException("User not found: " + username)));
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

@Bean
public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
}

@Bean
public UserDetailsService userDetailsService(){
        return new UserDetailsService(){
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userRepo.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));
            }
        };
}

}

