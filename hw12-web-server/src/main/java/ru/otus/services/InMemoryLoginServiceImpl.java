package ru.otus.services;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.security.AbstractLoginService;
import org.eclipse.jetty.security.RolePrincipal;
import org.eclipse.jetty.security.UserPrincipal;
import org.eclipse.jetty.util.security.Password;
import ru.otus.dao.UserDao;
import ru.otus.model.Roles;
import ru.otus.model.User;

import java.util.List;
import java.util.Optional;

@Slf4j
public class InMemoryLoginServiceImpl extends AbstractLoginService {

    private final UserDao userDao;

    public InMemoryLoginServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }


    @Override
    protected List<RolePrincipal> loadRoleInfo(UserPrincipal userPrincipal) {

        Optional<User> dbUser = userDao.findByLogin(userPrincipal.getName());

        if (dbUser.isPresent()) {
            return dbUser.get().roles().stream()
                    .map(Roles::name)
                    .map(RolePrincipal::new)
                    .toList();
        }

        return List.of(new RolePrincipal(Roles.DEFAULT.name()));
    }

    @Override
    protected UserPrincipal loadUserInfo(String login) {
        log.info("InMemoryLoginService#loadUserInfo({})", login);
        Optional<User> dbUser = userDao.findByLogin(login);
        return dbUser.map(u -> new UserPrincipal(u.login(), new Password(u.password()))).orElse(null);
    }
}
