package io.github.code10.monumentumservice.service;

import io.github.code10.monumentumservice.controller.exception.BadRequestException;
import io.github.code10.monumentumservice.controller.exception.NotFoundException;
import io.github.code10.monumentumservice.model.domain.*;
import io.github.code10.monumentumservice.model.dto.*;
import io.github.code10.monumentumservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MonumentService {

    private final MonumentRepository monumentRepository;

    private final FavoriteMonumentRepository favoriteMonumentRepository;

    private final VisitedMonumentRepository visitedMonumentRepository;

    private final RatedMonumentRepository ratedMonumentRepository;

    private final CommentMonumentRepository commentMonumentRepository;

    @Autowired
    public MonumentService(MonumentRepository monumentRepository, FavoriteMonumentRepository favoriteMonumentRepository, VisitedMonumentRepository visitedMonumentRepository, RatedMonumentRepository ratedMonumentRepository, CommentMonumentRepository commentMonumentRepository) {
        this.monumentRepository = monumentRepository;
        this.favoriteMonumentRepository = favoriteMonumentRepository;
        this.visitedMonumentRepository = visitedMonumentRepository;
        this.ratedMonumentRepository = ratedMonumentRepository;
        this.commentMonumentRepository = commentMonumentRepository;
    }

    public List<Monument> findAll() {
        return monumentRepository.findAllByOrderByIdDesc();
    }

    public List<MonumentDetailDto> findAll(User user) {
        final List<Monument> monuments = monumentRepository.findAll();
        final List<MonumentDetailDto> monumentDetailDtos = monuments.stream().map(m -> findById(m.getId(), user)).collect(Collectors.toList());

        return monumentDetailDtos;
    }

    public Monument findById(long id) {
        return monumentRepository.findById(id).orElseThrow(() -> new NotFoundException("Monument not found!"));
    }

    public MonumentDetailDto findById(long id, User user) {
        final Monument monument = monumentRepository.findById(id).orElseThrow(() -> new NotFoundException("Monument not found!"));
        final UserRatedMonument ratedMonument = ratedMonumentRepository.findByUserAndMonument(user, monument);
        final UserFavoriteMonument favoriteMonument = favoriteMonumentRepository.findByUserAndMonument(user, monument);
        final UserVisitedMonument visitedMonument = visitedMonumentRepository.findByUserAndMonument(user, monument);
        final List<UserCommentDto> userCommentDtos = commentMonumentRepository.findByMonumentOrderByTimestampDesc(monument).stream()
                .map(c -> new UserCommentDto(new UserProfileDto(c.getUser()), c.getComment(), c.getTimestamp())).collect(Collectors.toList());

        return new MonumentDetailDto(monument, ratedMonument, favoriteMonument, visitedMonument, userCommentDtos);
    }

    public List<MonumentDetailDto> findFavorites(User user) {
        final List<Monument> monuments = favoriteMonumentRepository.findByUserOrderByMonumentDesc(user).stream().map(UserFavoriteMonument::getMonument).collect(Collectors.toList());
        return monuments.stream().map(m -> findById(m.getId(), user)).collect(Collectors.toList());
    }

    public UserFavoriteMonument findFavorite(User user, Monument monument) {
        return favoriteMonumentRepository.findByUserAndMonument(user, monument);
    }

    public List<MonumentDetailDto> findVisited(User user) {
        final List<Monument> monuments = visitedMonumentRepository.findByUserOrderByMonumentDesc(user).stream().map(UserVisitedMonument::getMonument).collect(Collectors.toList());
        return monuments.stream().map(m -> findById(m.getId(), user)).collect(Collectors.toList());
    }

    public UserVisitedMonument findVisited(User user, Monument monument) {
        return visitedMonumentRepository.findByUserAndMonument(user, monument);
    }

    public UserRatedMonument findRated(User user, Monument monument) {
        return ratedMonumentRepository.findByUserAndMonument(user, monument);
    }

    public Monument create(AddMonumentRequest monumentRequest, String path, User currentUser) {
        return monumentRepository.save(new Monument(monumentRequest.getName(), monumentRequest.getDescription(),
                path, currentUser, monumentRequest.getLocation(), monumentRequest.getLatitude(), monumentRequest.getLongitude()));
    }

    public String uploadImage(MultipartFile file) {
        final String uuid = UUID.randomUUID().toString();
        final String imageName = file.getOriginalFilename();
        final String imageUrl = String.format("src\\main\\resources\\img\\%s_%s", uuid, imageName);

        final File newImage = new File(imageUrl);
        try {
            if (newImage.exists()) {
                Files.delete(newImage.toPath());
            }

            Files.createFile(newImage.toPath());
        } catch (IOException e) {
            throw new BadRequestException("Failed to upload image!");
        }

        try (final BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(newImage))) {
            final byte[] bytes = file.getBytes();
            stream.write(bytes);
        } catch (IOException e) {
            throw new BadRequestException("Failed to upload image!");
        }

        return imageUrl;
    }

    public void updateFavorite(User currentUser, UpdateFavoriteMonumentRequest request) {
        final Monument monument = findById(request.getMonumentId());
        final UserFavoriteMonument favoriteMonument = findFavorite(currentUser, monument);

        if (favoriteMonument == null && request.isFavorite()) {
            favoriteMonumentRepository.save(new UserFavoriteMonument(currentUser, monument));
        } else if (favoriteMonument != null && !request.isFavorite()) {
            favoriteMonumentRepository.delete(favoriteMonument);
        }
    }

    public void updateVisited(User currentUser, UpdateVisitedMonumentRequest request) {
        final Monument monument = findById(request.getMonumentId());
        final UserVisitedMonument visitedMonument = findVisited(currentUser, monument);

        if (visitedMonument == null && request.isVisited()) {
            visitedMonumentRepository.save(new UserVisitedMonument(currentUser, monument));
        } else if (visitedMonument != null && !request.isVisited()) {
            visitedMonumentRepository.delete(visitedMonument);
        }
    }

    public void updateRating(User currentUser, UpdateMonumentRatingRequest request) {
        final Monument monument = findById(request.getMonumentId());
        UserRatedMonument ratedMonument = findRated(currentUser, monument);
        final int newRatedCount = ratedMonument == null ? monument.getRatedCount() + 1 : monument.getRatedCount();
        double ratingSum = monument.getAverageRating() * monument.getRatedCount() + request.getRating();

        if (ratedMonument == null) {
            ratedMonument = new UserRatedMonument(currentUser, monument, request.getRating());
        } else {
            // if user already rated the monument before we need to subtract the previous value
            ratingSum -= ratedMonument.getRating();
            ratedMonument.setRating(request.getRating());
        }

        monument.setAverageRating(ratingSum / newRatedCount);
        monument.setRatedCount(newRatedCount);

        monumentRepository.save(monument);
        ratedMonumentRepository.save(ratedMonument);
    }

    public UserCommentDto addComment(User currentUser, AddCommentRequest request) {
        final Monument monument = findById(request.getMonumentId());
        final UserCommentMonument userCommentMonument = new UserCommentMonument(currentUser, monument, request.getComment());

        commentMonumentRepository.save(userCommentMonument);
        return new UserCommentDto(new UserProfileDto(currentUser), request.getComment(), userCommentMonument.getTimestamp());
    }

    public byte[] loadImage(String imageUri) {
        try {
            final File imgPath = new File(imageUri);
            return Files.readAllBytes(imgPath.toPath());
        } catch (IOException e) {
            throw new NotFoundException("Image not found");
        }
    }
}
