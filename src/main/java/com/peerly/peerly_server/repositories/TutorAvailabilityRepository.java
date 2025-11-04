package com.peerly.peerly_server.repositories;

import com.peerly.peerly_server.models.TutorAvailability;
import org.springframework.data.repository.ListCrudRepository;
import java.util.List;

public interface TutorAvailabilityRepository extends ListCrudRepository<TutorAvailability, String> {
    List<TutorAvailability> findByTutor_IdAndWeekday(String tutorId, int weekday);
}
