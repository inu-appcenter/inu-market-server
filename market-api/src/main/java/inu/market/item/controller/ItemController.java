package inu.market.item.controller;

import inu.market.config.LoginUser;
import inu.market.item.dto.ItemCreateRequest;
import inu.market.item.dto.ItemResponse;
import inu.market.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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

}
