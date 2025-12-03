package com.peerly.peerly_server.repositories;

import com.peerly.peerly_server.models.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TutorRepository extends JpaRepository<Tutor, String> {}
