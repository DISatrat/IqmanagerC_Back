package org.iqmanager.config.security;

import org.iqmanager.models.UserLoginData;
import org.iqmanager.repository.UserLoginDataDAO;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserLoginDataDAO userLoginDataDAO;

    public UserDetailsServiceImpl(UserLoginDataDAO userLoginDataDAO) {
        this.userLoginDataDAO = userLoginDataDAO;
    }


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserLoginData myUser = userLoginDataDAO.findByUsername(userName);
        if (myUser == null) {
            throw new UsernameNotFoundException("Unknown user: "+ userName);
        }
        return User.builder()
                .username(myUser.getUsername())
                .password(myUser.getPassword())
                .roles(myUser.getAuthority())
                .build();
    }
}
