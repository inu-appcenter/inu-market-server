package inu.market.item.controller;

import inu.market.config.LoginUser;
import inu.market.item.dto.ItemCreateRequest;
import inu.market.item.dto.ItemResponse;
import inu.market.item.dto.ItemUpdateRequest;
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
            throw new RuntimeException("파일이 존재하지 않습니다.");
        }

        Map<String, List<String>> response = new HashMap<>();
        List<String> imageUrls = itemService.uploadImages(images);
        response.put("imageUrls", imageUrls);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/items")
    public ResponseEntity<ItemResponse> create(@LoginUser Long userId,
                                               @RequestBody @Valid ItemCreateRequest request) {
        return ResponseEntity.ok(itemService.create(userId, request));
    }

    @PutMapping("/api/items/{itemId}")
    public ResponseEntity<ItemResponse> update(@LoginUser Long userId,
                                               @PathVariable Long itemId,
                                               @RequestBody @Valid ItemUpdateRequest request) {
        return ResponseEntity.ok(itemService.update(userId, itemId, request));
    }

    @DeleteMapping("/api/items/{itemId}")
    public ResponseEntity<Void> delete(@LoginUser Long userId,
                                       @PathVariable Long itemId) {
        itemService.delete(userId, itemId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/api/items/{itemId}")
    public ResponseEntity<ItemResponse> findById(@PathVariable Long itemId) {
        return ResponseEntity.ok(itemService.findById(itemId));
    }
}
