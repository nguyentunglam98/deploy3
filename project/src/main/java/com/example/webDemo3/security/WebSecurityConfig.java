package com.example.webDemo3.security;

import com.example.webDemo3.constant.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${userUrl}")
    private String[] userUrl ;

    @Value("${adminUrl}")
    private String[] adminUrl ;

    @Value("${manageTimetableUrl}")
    private String[] manageTimetableUrl ;

    @Value("${gradingToEmulationUrl}")
    private String[] gradingToEmulationUrl ;

    @Value("${viewRequestUrl}")
    private String[] viewRequestUrl ;

    @Value("${newletterUrl}")
    private String[] newletterUrl ;

    @Autowired
    private UserService userService;

    private String ROLE_ADMIN = "ROLE_" + Constant.ROLEID_ADMIN;
    private String ROLE_TIMETABLE_MANAGER = "ROLE_" + Constant.ROLEID_TIMETABLE_MANAGER;
    private String ROLE_REDSTAR = "ROLE_" + Constant.ROLEID_REDSTAR;
    private String ROLE_MONITOR = "ROLE_" + Constant.ROLEID_MONITOR;
    private String ROLE_SUMMERIZEGROUP = "ROLE_" + Constant.ROLEID_SUMMERIZEGROUP;
    private String ROLE_CLUBLEADER = "ROLE_" + Constant.ROLEID_CLUBLEADER;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        // Get AuthenticationManager bean
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Password encoder, để Spring Security sử dụng mã hóa mật khẩu người dùng
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userService) // Cung cáp userservice cho spring security
                .passwordEncoder(passwordEncoder()); // cung cấp password encoder
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf()
                .disable()
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers(userUrl)
                .permitAll()
               // .antMatchers(adminUrl).hasRole(ROLE_ADMIN)
                .antMatchers(adminUrl).access("hasRole('" + ROLE_ADMIN + "')")
                .antMatchers(manageTimetableUrl).access("hasAnyRole('"+ ROLE_ADMIN +"','"+ ROLE_TIMETABLE_MANAGER +"')")
                .antMatchers(gradingToEmulationUrl).access("hasAnyRole('"+ ROLE_ADMIN +"','"+ ROLE_REDSTAR +"','"+ ROLE_SUMMERIZEGROUP +"')")
                .antMatchers(viewRequestUrl).access("hasAnyRole('"+ ROLE_ADMIN +"','"+ ROLE_MONITOR +"')")
                .antMatchers(newletterUrl).access("hasAnyRole('"+ ROLE_ADMIN +"','"+ ROLE_MONITOR +"','"+ ROLE_CLUBLEADER +"')")
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new CustomAccessDeniedHandler())
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
