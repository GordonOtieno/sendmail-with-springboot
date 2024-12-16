package com.gordon.gmail_sender.controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gordon.gmail_sender.entity.HttpResponse;
import com.gordon.gmail_sender.entity.User;
import com.gordon.gmail_sender.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UsersController {
	private final UserService userService;
	
	@PostMapping
	public ResponseEntity<HttpResponse> createUser(@RequestBody User user) {
		User newUser = userService.saveUser(user);
				
		return ResponseEntity.created(URI.create("")).body(
          HttpResponse.builder()
          .timestamp(LocalDateTime.now().toString())
          .data(Map.of("user", newUser))
          .message("User Created successfully")
          .status(HttpStatus.CREATED)
          .statuscode(HttpStatus.CREATED.value()).build()
          );
	}
	
	@GetMapping
	public ResponseEntity<HttpResponse> ConfirmUserAccount(@RequestParam("token")String token) {
		Boolean  issuccess = userService.verifyToken(token);
				
		return ResponseEntity.ok().body(
          HttpResponse.builder()
          .timestamp(LocalDateTime.now().toString())
          .data(Map.of("Success", issuccess))
          .message("Account Verified successfully")
          .status(HttpStatus.OK)
          .statuscode(HttpStatus.OK.value())
          .build()
          );
	}

}
