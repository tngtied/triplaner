package com.tngtied.triplaner.service;

import com.tngtied.triplaner.entity.SiteUser;
import com.tngtied.triplaner.repository.UserRepository;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SiteUser create(String userName, String email, String password) throws ConstraintViolationException {
        SiteUser siteUser = new SiteUser();
        siteUser.setUsername(userName);
        siteUser.setEmail(email);

        if (password.length()<8 || password.length()>20){
            throw new ConstraintViolationException("password length must be between 8 and 20", null);
        }

        siteUser.setPassword(passwordEncoder.encode(password));
        this.userRepository.save(siteUser);
        return siteUser;
    }
}
