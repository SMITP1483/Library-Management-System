package org.example.librarymanagementsystem.Service;

import org.example.librarymanagementsystem.Repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService {
    @Autowired
    private UserDetailsRepository userRepository;
}
