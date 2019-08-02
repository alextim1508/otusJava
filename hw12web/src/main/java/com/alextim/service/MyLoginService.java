package com.alextim.service;

import com.alextim.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.security.AbstractLoginService;
import org.eclipse.jetty.util.security.Credential;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor @Slf4j
public class MyLoginService extends AbstractLoginService {

    private final ServiceDB service;

    @Override
    protected String[] loadRoleInfo(UserPrincipal userPrincipal) {
        List<User> byNames = service.load(userPrincipal.getName());
        if(byNames.isEmpty())
            return new String[0];
        User userFromDb = byNames.get(0);
        List<String> roles = userFromDb.getRoles();
        return roles.toArray(new String[roles.size()]);
    }

    @Override
    protected UserPrincipal loadUserInfo(String userName) {
        List<User> byNames = service.load(userName);
        if(byNames.isEmpty()) {
            log.info("Not found users by name {}", userName);
            return null;
        }
        User userFromDb = byNames.get(0);
        log.info("Load user {} from data base", userFromDb);
        return new UserPrincipal(userFromDb.getName(), Credential.getCredential(userFromDb.getCryptPassword()));
    }
}
