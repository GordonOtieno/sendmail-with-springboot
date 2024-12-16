package com.gordon.gmail_sender.service;

import org.springframework.stereotype.Service;

import com.gordon.gmail_sender.entity.User;

@Service
public interface UserService {
	User saveUser(User user);
	boolean verifyToken(String token);

}
