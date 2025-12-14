package com.peerly.peerly_server.repositories;

import com.peerly.peerly_server.models.Subject;
import org.springframework.data.repository.ListCrudRepository;

public interface SubjectRepository extends ListCrudRepository<Subject, String> {}
