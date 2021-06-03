package br.com.devaria.projetos.gerenciadortarefas.configurations

import br.com.devaria.projetos.gerenciadortarefas.filters.JWTAutorizadorFilter
import br.com.devaria.projetos.gerenciadortarefas.utils.JWTUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

@Configuration
@EnableWebSecurity
class SecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var jwtUtils: JWTUtils

    override fun configure(http: HttpSecurity) {
        http.csrf().disable().authorizeRequests()
            .antMatchers(HttpMethod.POST, "/api/login").permitAll()
            .anyRequest().authenticated()

        http.addFilter(JWTAutorizadorFilter(authenticationManager(), jwtUtils))
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }
}