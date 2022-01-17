package com.bluntsoftware.shirtshop.tenant;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class TenantJwtRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        return getRoles(jwt).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public List<String> getRoles(Jwt jwt){
        return TenantUserService.getUser(jwt.getTokenValue()).orElseThrow(()->{
            throw new RuntimeException("Tenant User not Found");
        }).getRoles();
    }
}

