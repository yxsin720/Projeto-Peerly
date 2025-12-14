package com.peerly.peerly_server.controllers;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.peerly.peerly_server.models.dto.CreateReviewRequest;
import com.peerly.peerly_server.models.responses.Review;
import com.peerly.peerly_server.services.ReviewService;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public Review create(@RequestBody CreateReviewRequest dto) {
        return reviewService.createReview(dto);
    }

    @GetMapping
    public List<Review> getAll() {
        return reviewService.getAll();
    }

    @GetMapping("/tutor/{tutorId}")
    public List<Review> getByTutor(@PathVariable String tutorId) {
        return reviewService.getByTutorId(tutorId);
    }
}
