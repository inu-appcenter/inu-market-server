package inu.market.item.controller;

import inu.market.common.NotExistException;
import inu.market.config.LoginUser;
import inu.market.item.dto.*;
import inu.market.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/api/items/imageUrls")
    public ResponseEntity<Map<String, List<String>>> convertToImageUrls(List<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            throw new NotExistException("상품 이미지가 없습니다.");
        }

        Map<String, List<String>> response = new HashMap<>();
        List<String> imageUrls = itemService.uploadImages(images);
        response.put("imageUrls", imageUrls);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/items")
    public ResponseEntity<Map<String, Long>> create(@LoginUser Long userId,
                                                    @RequestBody @Valid ItemCreateRequest request) {
        Long itemId = itemService.create(userId, request);
        Map<String, Long> response = new HashMap<>();
        response.put("itemId", itemId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/items/{itemId}")
    public ResponseEntity<Void> update(@LoginUser Long userId,
                                       @PathVariable Long itemId,
                                       @RequestBody @Valid ItemUpdateRequest request) {
        itemService.update(userId, itemId, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/api/items/{itemId}")
    public ResponseEntity<Void> updateStatus(@LoginUser Long userId,
                                             @PathVariable Long itemId,
                                             @RequestBody @Valid ItemUpdateStatusRequest request) {
        itemService.updateStatus(userId, itemId, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/api/items/{itemId}")
    public ResponseEntity<Void> delete(@LoginUser Long userId,
                                       @PathVariable Long itemId) {
        itemService.delete(userId, itemId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/api/items/{itemId}")
    public ResponseEntity<ItemResponse> findById(@LoginUser Long userId, @PathVariable Long itemId) {
        return ResponseEntity.ok(itemService.findById(userId, itemId));
    }

    @GetMapping("/api/items")
    public ResponseEntity<List<ItemResponse>> findBySearchRequest(@LoginUser Long userId, @Valid ItemSearchRequest request) {
        return ResponseEntity.ok(itemService.findBySearchRequest(userId, request));
    }

    @GetMapping("/api/users/items")
    public ResponseEntity<List<ItemResponse>> findBySeller(@LoginUser Long userId) {
        return ResponseEntity.ok(itemService.findBySeller(userId));
    }

}
