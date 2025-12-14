package com.peerly.peerly_server.services;

import org.springframework.stereotype.Service;
import java.util.List;

import com.peerly.peerly_server.models.dto.CreateReviewRequest;
import com.peerly.peerly_server.models.responses.Review;
import com.peerly.peerly_server.models.Session;
import com.peerly.peerly_server.models.User;
import com.peerly.peerly_server.repositories.ReviewRepository;
import com.peerly.peerly_server.repositories.SessionRepository;
import com.peerly.peerly_server.repositories.UserRepository;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    public ReviewService(
            ReviewRepository reviewRepository,
            SessionRepository sessionRepository,
            UserRepository userRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    public Review createReview(CreateReviewRequest dto) {
        Session session = sessionRepository.findById(dto.getSessionId())
                .orElseThrow(() -> new RuntimeException("Sessão não encontrada"));

        User reviewer = userRepository.findById(dto.getReviewerId())
                .orElseThrow(() -> new RuntimeException("Revisor não encontrado"));

        User tutor = userRepository.findById(dto.getTutorId())
                .orElseThrow(() -> new RuntimeException("Tutor não encontrado"));

        Review review = new Review();
        review.setSession(session);
        review.setReviewer(reviewer);
        review.setTutor(tutor);
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());

        return reviewRepository.save(review);
    }

    public List<Review> getAll() {
        return reviewRepository.findAll();
    }

    public List<Review> getByTutorId(String tutorId) {
        return reviewRepository.findByTutorId(tutorId);
    }
}
