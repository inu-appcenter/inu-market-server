package inu.market.category.controller;

import inu.market.category.dto.CategoryCreateRequest;
import inu.market.category.dto.CategoryResponse;
import inu.market.category.dto.CategoryUpdateRequest;
import inu.market.category.service.CategoryService;
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
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/api/categories/iconUrls")
    public ResponseEntity<Map<String, String>> convertToIconUrl(@RequestPart MultipartFile image) {

        if (image == null || image.isEmpty()) {
            throw new RuntimeException("업로드 할 이미지가 없습니다.");
        }

        Map<String, String> response = new HashMap<>();
        String iconUrl = categoryService.uploadImage(image);
        response.put("iconUrl", iconUrl);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/categories")
    public ResponseEntity<CategoryResponse> create(@RequestBody @Valid CategoryCreateRequest request) {
        return ResponseEntity.ok(categoryService.create(request));
    }

    @PutMapping("/api/categories/{categoryId}")
    public ResponseEntity<Void> update(@PathVariable Long categoryId,
                                       @RequestBody @Valid CategoryUpdateRequest request) {
        categoryService.update(categoryId, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/api/categories/{categoryId}")
    public ResponseEntity<Void> delete(@PathVariable Long categoryId) {
        categoryService.delete(categoryId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/api/categories")
    public ResponseEntity<List<CategoryResponse>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }


}
