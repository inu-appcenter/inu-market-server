package inu.market.favorite.controller;

import inu.market.config.LoginUser;
import inu.market.favorite.dto.FavoriteCreateRequest;
import inu.market.favorite.dto.FavoriteDeleteRequest;
import inu.market.favorite.service.FavoriteService;
import inu.market.item.dto.ItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/api/favorites")
    public ResponseEntity<Void> create(@LoginUser Long userId,
                                       @RequestBody @Valid FavoriteCreateRequest request) {
        favoriteService.create(userId, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/api/favorites")
    public ResponseEntity<Void> delete(@LoginUser Long userId,
                                       @RequestBody @Valid FavoriteDeleteRequest request) {
        favoriteService.delete(userId, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/api/favorites")
    public ResponseEntity<List<ItemResponse>> findByUserId(@LoginUser Long userId) {
        return ResponseEntity.ok(favoriteService.findByUserId(userId));
    }

}
