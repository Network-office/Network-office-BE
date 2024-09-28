package dev.office.networkoffice.gathering.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.office.networkoffice.gathering.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
