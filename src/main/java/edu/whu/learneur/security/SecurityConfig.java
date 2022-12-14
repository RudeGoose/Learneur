package edu.whu.learneur.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {
    @Autowired
    JwtFilter filter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        security.csrf().disable();
        security.authorizeRequests()
                .antMatchers("/authenticate/**").permitAll()     // 登录注册界面通告
                .antMatchers("/knowledge*").permitAll()           // 知识图谱界面通告
                .antMatchers("/knowledge/*").permitAll()          // 用户界面通告
                .antMatchers("/relation*").permitAll()            // 知识图谱关系界面通告
                .antMatchers("/index").permitAll()                  // 首页都通过
                .antMatchers("/videos/**").permitAll()
                .antMatchers("/projects/**").permitAll()
                .antMatchers("/tutorials").permitAll()
                .antMatchers("/books/**").permitAll()
                .antMatchers("/mysql-knowledge/**").permitAll()
                .antMatchers("/resource/**").permitAll()
                .antMatchers("/notes/**").permitAll()
                .antMatchers("/users/**").permitAll()
                .antMatchers("/admin/").hasAuthority("admin")
                .antMatchers("/search/*").permitAll()
                .anyRequest().authenticated()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
        return security.build();
    }
}
