package com.gordon.gmail_sender.service.impl;

import org.springframework.stereotype.Service;

import com.gordon.gmail_sender.entity.Confirmation;
import com.gordon.gmail_sender.entity.User;
import com.gordon.gmail_sender.repository.ConfirmationRepository;
import com.gordon.gmail_sender.repository.UserRepository;
import com.gordon.gmail_sender.service.EmailService;
import com.gordon.gmail_sender.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
   private final UserRepository userRepository;
   private final ConfirmationRepository confirmationRepository;
   private final EmailService emailService;
   
	@Override
	public User saveUser(User user) {
		if(userRepository.existsByEmail(user.getEmail())) {
			throw new RuntimeException("User with the specified Id already exist");
		}
		user.setIsenabled(false);
		userRepository.save(user);
		
		Confirmation confirmation = new Confirmation(user);
		confirmationRepository.save(confirmation);
		
		//send an email to user with the token
		//emailService.sendSimpleMailMessage(user.getName(),user.getEmail(), confirmation.getToken());
		//emailService.sendMimeMessageWithAttachement(user.getName(),user.getEmail(), confirmation.getToken());
		//emailService.sendMimeMessageWithEmbeddedImages(user.getName(),user.getEmail(), confirmation.getToken());
		//emailService.sendHtmlEmail(user.getName(),user.getEmail(), confirmation.getToken());
		emailService.sendHtmlEmailWithEmbeddedFiles(user.getName(),user.getEmail(), confirmation.getToken());
		

		return user;
	}

	@Override
	public boolean verifyToken(String token) {
		Confirmation confirmation = confirmationRepository.findByToken(token);
		User user = userRepository.findByEmailIgnoreCase(confirmation.getUser().getEmail());
		user.setIsenabled(true);
		userRepository.save(user);
	    confirmationRepository.delete(confirmation);
		return true;
	}

}
