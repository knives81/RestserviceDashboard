package com.dashboard.restservicedashboard;

import com.dashboard.restservicedashboard.admin.Account;
import com.dashboard.restservicedashboard.admin.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
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
            return new User(account.getUsername(), account.getPassword(), true, true, true, true,
                    AuthorityUtils.createAuthorityList("USER"));
        } else {
            System.out.println("could not find the user '" + username + "'");
            throw new UsernameNotFoundException("could not find the user '" + username + "'");

        }
    }
}