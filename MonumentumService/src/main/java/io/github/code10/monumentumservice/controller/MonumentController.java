package io.github.code10.monumentumservice.controller;

import io.github.code10.monumentumservice.model.domain.Monument;
import io.github.code10.monumentumservice.model.domain.User;
import io.github.code10.monumentumservice.model.dto.*;
import io.github.code10.monumentumservice.service.MonumentService;
import io.github.code10.monumentumservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("api/monuments")
public class MonumentController {

    private final MonumentService monumentService;

    private final UserService userService;

    @Autowired
    public MonumentController(MonumentService monumentService, UserService userService) {
        this.monumentService = monumentService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity findById(@PathVariable long id) {
        final User currentUser = userService.findCurrentUser();
        final MonumentDetailDto monument = monumentService.findById(id, currentUser);

        return new ResponseEntity<>(monument, HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity findAll() {
        final List<Monument> monuments = monumentService.findAll();
        return new ResponseEntity<>(monuments, HttpStatus.OK);
    }

    @GetMapping("/favorite")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity findFavorites() {
        final User currentUser = userService.findCurrentUser();
        final List<MonumentDetailDto> monuments = monumentService.findFavorites(currentUser);

        return new ResponseEntity<>(monuments, HttpStatus.OK);
    }

    @GetMapping("/visited")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity findVisited() {
        final User currentUser = userService.findCurrentUser();
        final List<MonumentDetailDto> monuments = monumentService.findVisited(currentUser);

        return new ResponseEntity<>(monuments, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity add(@RequestParam("name") String name, @RequestParam("description") String description,
                              @RequestParam("location") String location, @RequestParam("longitude") double longitude,
                              @RequestParam("latitude") double latitude, @RequestParam("img") @Valid @NotNull MultipartFile file) {
        final User currentUser = userService.findCurrentUser();
        final String path = monumentService.uploadImage(file);
        if (path == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        final AddMonumentRequest monumentRequest = new AddMonumentRequest(name, description, location, longitude, latitude);
        final Monument monument = monumentService.create(monumentRequest, path, currentUser);

        return new ResponseEntity<>(monument.getId(), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}/image", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity getImage(@PathVariable long id) {
        final Monument monument = monumentService.findById(id);
        final byte[] imageBytes = monumentService.loadImage(monument.getImageUri());

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

    @PostMapping("/mark-favorite")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity markFavorite(@RequestBody UpdateFavoriteMonumentRequest request) {
        final User currentUser = userService.findCurrentUser();
        monumentService.updateFavorite(currentUser, request);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/mark-visited")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity markVisited(@RequestBody UpdateVisitedMonumentRequest request) {
        final User currentUser = userService.findCurrentUser();
        monumentService.updateVisited(currentUser, request);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/rate")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity rate(@Valid @RequestBody UpdateMonumentRatingRequest request) {
        final User currentUser = userService.findCurrentUser();
        monumentService.updateRating(currentUser, request);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/comment")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity addComment(@Valid @RequestBody AddCommentRequest request) {
        final User currentUser = userService.findCurrentUser();
        final UserCommentDto userCommentDto = monumentService.addComment(currentUser, request);

        return new ResponseEntity<>(userCommentDto, HttpStatus.CREATED);
    }
}
