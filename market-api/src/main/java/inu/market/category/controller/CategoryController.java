package inu.market.category.controller;

import inu.market.category.domain.Category;
import inu.market.category.dto.CategoryCreateRequest;
import inu.market.category.dto.CategoryResponse;
import inu.market.category.dto.CategoryUpdateRequest;
import inu.market.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @DeleteMapping("/api/categories/{categoryId}")
    public ResponseEntity<CategoryResponse> delete(@PathVariable Long categoryId) {
        categoryService.delete(categoryId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
