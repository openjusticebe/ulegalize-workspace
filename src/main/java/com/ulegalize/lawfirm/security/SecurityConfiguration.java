package com.ulegalize.lawfirm.security;


import com.ulegalize.lawfirm.security.handler.ForbiddenHandler;
import com.ulegalize.lawfirm.security.handler.UnauthorizedHandler;
import com.ulegalize.security.EnumRights;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Slf4j
public class SecurityConfiguration {

    @Value("${ulegalize.http.admin-auth-token-header-name}")
    private String principalAdminRequestHeader;

    @Value("${ulegalize.http.admin-auth-token}")
    private String principalAdminRequestValue;

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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        APIKeyAuthFilter filterAdmin = new APIKeyAuthFilter(principalAdminRequestHeader);

        // check first webhook
        filterAdmin.setAuthenticationManager(new AuthenticationManager() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                String principal = (String) authentication.getPrincipal();
                if (!principalAdminRequestValue.equals(principal)) {
                    throw new BadCredentialsException("The API key was not found or not the expected value.");
                }
                authentication.setAuthenticated(true);
                return authentication;
            }
        });

        httpSecurity.cors().and()
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()

                .addFilter(filterAdmin)
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
                .antMatchers("/v2/admin/security/approveWorkspace").permitAll()
                // swagger
                .antMatchers("/v3/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**").permitAll()

                .anyRequest().authenticated();

        // custom JWT based security filter
        httpSecurity.addFilterBefore(authenticationFilterBean(), UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        httpSecurity.headers().cacheControl();

        return httpSecurity.build();
    }
}
