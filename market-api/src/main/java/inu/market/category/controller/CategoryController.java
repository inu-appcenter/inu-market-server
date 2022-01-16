package inu.market.category.controller;

import inu.market.category.dto.CategoryCreateRequest;
import inu.market.category.dto.CategoryResponse;
import inu.market.category.dto.CategoryUpdateRequest;
import inu.market.category.service.CategoryService;
import inu.market.common.NotExistException;
import inu.market.common.NotFoundException;
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
            throw new NotExistException("이미지가 없습니다.");
        }

        Map<String, String> response = new HashMap<>();
        String iconUrl = categoryService.uploadImage(image);
        response.put("iconUrl", iconUrl);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/categories")
    public ResponseEntity<Map<String, Long>> create(@RequestBody @Valid CategoryCreateRequest request) {
        Long categoryId = categoryService.create(request);
        Map<String, Long> response = new HashMap<>();
        response.put("categoryId", categoryId);
        return ResponseEntity.ok(response);
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
