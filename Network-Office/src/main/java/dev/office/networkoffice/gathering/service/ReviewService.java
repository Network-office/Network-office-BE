package dev.office.networkoffice.gathering.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.office.networkoffice.gathering.entity.DeletedGathering;
import dev.office.networkoffice.gathering.entity.Review;
import dev.office.networkoffice.gathering.repository.ReviewRepository;
import dev.office.networkoffice.user.entity.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

//    public Optional<Review> findDeletedGatheringByPastId(Long gatheringId) {
////        return reviewRepository.findByOriginGatheringId(gatheringId);
//    }

    @Transactional
    public Review savingDeletedGathering(User host, DeletedGathering gathering) {
        return reviewRepository.save(
                Review.builder()
                        .user(host)
                        .deletedGathering(gathering)
                        .build()
        );
    }
}
