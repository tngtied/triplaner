package com.tngtied.triplaner.service;

import com.tngtied.triplaner.UserRole;
import com.tngtied.triplaner.entity.Member;
import com.tngtied.triplaner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> user = this.userRepository.findByusername(username);
        if (user.isEmpty()){
            throw new UsernameNotFoundException("SiteUser not found.");
        }
        Member siteUserFound = user.get();
        List<GrantedAuthority> authorities = new ArrayList<>();

        if ("admin".equals(siteUserFound.getUsername())){
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        }else{
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }

        return new User(siteUserFound.getUsername(), siteUserFound.getPassword(), authorities);

    }


}
