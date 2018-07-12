package com.dashboard.restservicedashboard;

import com.dashboard.commondashboard.Account;
import com.dashboard.commondashboard.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService  {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println(username);
        Account account = accountRepository.findByUsername(username);
        if (account != null) {
            return User.withUsername(account.getUsername())
                    .password(account.getPassword())
                    .roles("ÄDMIN").build();
        } else {
            System.out.println("could not find the user '" + username + "'");
            throw new UsernameNotFoundException("could not find the user '" + username + "'");

        }
    }
}