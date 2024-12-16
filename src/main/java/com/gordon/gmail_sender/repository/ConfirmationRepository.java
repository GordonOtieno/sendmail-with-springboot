package com.gordon.gmail_sender.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gordon.gmail_sender.entity.Confirmation;

@Repository
public interface ConfirmationRepository extends JpaRepository<Confirmation, Long> {
		//Confirmation existsByUserEmailIgnorecase(String email);
		 Confirmation findByToken(String token);
		 Boolean existsByUserEmail(String email);

		}

	

