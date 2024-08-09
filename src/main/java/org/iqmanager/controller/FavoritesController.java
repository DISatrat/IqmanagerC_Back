package org.iqmanager.controller;

import org.iqmanager.dto.FavoritesDTO;
import org.iqmanager.service.favoriteService.FavoriteService;
import org.iqmanager.service.postService.PostService;
import org.iqmanager.service.userDataService.UserDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.iqmanager.ApplicationC.URL_WEB;

@RestController
@RequestMapping("/api")
@Validated
@CrossOrigin(origins = URL_WEB)
public class FavoritesController {

    private final Logger logger = LoggerFactory.getLogger(FavoritesController.class);
    private final FavoriteService favoriteService;
    private final UserDataService userDataService;
    private final PostService postService;
    public FavoritesController(FavoriteService favoriteService, UserDataService userDataService, PostService postService) {
        this.favoriteService = favoriteService;
        this.userDataService = userDataService;
        this.postService = postService;
    }
    /** Избранное */
    @PostMapping("/favorites")
    public ResponseEntity<List<FavoritesDTO>> favorites(@RequestBody(required = false) long[] favorites) {
        try {
            if(userDataService.hasUserLoginned()) {
                return ResponseEntity.ok(favoriteService.getAll().stream().map(FavoritesDTO::favoritesToDTO).collect(Collectors.toList()));
            } else {
                return ResponseEntity.ok(Arrays.stream(favorites).mapToObj(x -> FavoritesDTO.favoritesToDTO(postService.getPost(x))).collect(Collectors.toList()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("FavoritesController -> favorites ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
    /** Удалить из избранного */
    @DeleteMapping("/deleteFav/{id}")
    public ResponseEntity<String> deleteFavorites(@PathVariable("id") long id) {
        try {
            favoriteService.delete(id);
            return ResponseEntity.ok("Successfully");
        } catch (Exception e) {
            logger.warn("FavoritesController -> deleteFavorites ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during removal");
        }
    }
    /** Очистить избранное */
    @DeleteMapping("/delFavorites")
    public ResponseEntity<String> deleteAllFavorites() {
        try {
            favoriteService.deleteAll();
            return ResponseEntity.ok("Successfully");
        } catch (Exception e) {
            logger.warn("FavoritesController -> deleteAllFavorites ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during removal");
        }
    }
    @PostMapping("/addFArr")
    public ResponseEntity<String> addFavoritesArr (@RequestBody @Validated long [] idArray) {
        if(userDataService.hasUserLoginned()) {
            try {
                for(long id : idArray) {
                    userDataService.addToFavorite(id);
                }
                return ResponseEntity.ok("Successfully");
            } catch (Exception e) {
                e.printStackTrace();
                logger.warn("PostController -> addFavorites ERROR");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during add");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
        }
    }
}