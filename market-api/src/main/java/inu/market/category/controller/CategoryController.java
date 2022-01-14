package inu.market.category.controller;

import inu.market.category.dto.CategoryCreateRequest;
import inu.market.category.dto.CategoryResponse;
import inu.market.category.dto.CategoryUpdateRequest;
import inu.market.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/api/categories")
    public ResponseEntity<CategoryResponse> create(@RequestBody @Valid CategoryCreateRequest request) {
        return ResponseEntity.ok(categoryService.create(request));
    }

    @PutMapping("/api/categories/{categoryId}")
    public ResponseEntity<CategoryResponse> update(@PathVariable Long categoryId,
                                                   @RequestBody @Valid CategoryUpdateRequest request) {
        return ResponseEntity.ok(categoryService.update(categoryId, request));
    }


}
