package boki.bokiportfolio.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@ConditionalOnProperty(name = ["server.port"], havingValue = "9090")
class ActuatorSecurityConfig {

    @Bean
    fun actuatorSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .httpBasic { it.disable() }
//            .formLogin { it.permitAll() }
            .formLogin {
                it.loginPage("/login").defaultSuccessUrl("/management", true).permitAll()
            }
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.ALWAYS) }
            .authorizeHttpRequests {
                it.requestMatchers("/login").permitAll()
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                    .requestMatchers("/management/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
            }

        return http.build()
    }

    @Bean
    fun userDetailsService(passwordEncoder: PasswordEncoder): UserDetailsService {
        val userDetailsManager = InMemoryUserDetailsManager()
        val admin = User.withUsername("manager")
            .password(passwordEncoder.encode("admin2024!@"))
            .roles("ADMIN")
            .build()
        userDetailsManager.createUser(admin)
        return userDetailsManager
    }
}
