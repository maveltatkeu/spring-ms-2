package com.thecodealchemist.main.security;

import com.thecodealchemist.main.service.ApiKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyAuthProvider implements AuthenticationProvider {
    @Autowired
    private ApiKeyService apiKeyService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String key = (String) authentication.getPrincipal();
        //validate the key by comparing it to KeyStore dummy value
        if(apiKeyService.isKeyValid(key)) {
            return new UsernamePasswordAuthenticationToken(key, null, null);
        }

        throw new BadCredentialsException("Invalid key = " + key);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
