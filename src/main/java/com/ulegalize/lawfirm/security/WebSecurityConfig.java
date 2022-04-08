package com.ulegalize.lawfirm.security;


import com.ulegalize.lawfirm.security.handler.ForbiddenHandler;
import com.ulegalize.lawfirm.security.handler.UnauthorizedHandler;
import com.ulegalize.security.EnumRights;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UnauthorizedHandler unauthorizedHandler() throws Exception {
        return new UnauthorizedHandler();
    }

    @Bean
    public ForbiddenHandler forbiddenHandler() throws Exception {
        return new ForbiddenHandler();
    }

    @Bean
    public AuthenticationFilter authenticationFilterBean() throws Exception {
        return new AuthenticationFilter();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and()
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()

                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler()).and()
                .exceptionHandling().accessDeniedHandler(forbiddenHandler()).and()

                // don't create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeRequests()

                // allow auth url
                .antMatchers("/v2/public/**").permitAll()
                .antMatchers("/public/**").permitAll()
                .antMatchers("/v2/drive/invoice").permitAll()
                .antMatchers("/v2/login/verifyUser").permitAll()
                .antMatchers("/v2/compta").hasAnyAuthority(EnumRights.ADMINISTRATEUR.name(), EnumRights.COMPTABILITE_LECTURE.name())
                .antMatchers("/v2/admin").hasAnyAuthority(EnumRights.ADMINISTRATEUR.name())
                .antMatchers("/v2/lawfirm/user/*/active").hasAnyAuthority(EnumRights.ADMINISTRATEUR.name())
                .antMatchers("/v1/backAdmin").hasAnyAuthority(EnumRights.SUPER_ADMIN.name())
                // swagger
                .antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**").permitAll()

                .anyRequest().authenticated();

        // custom JWT based security filter
        httpSecurity.addFilterBefore(authenticationFilterBean(), UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        httpSecurity.headers().cacheControl();
    }
}
