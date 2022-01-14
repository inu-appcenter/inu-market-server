package inu.market.category.controller;

import inu.market.category.dto.CategoryCreateRequest;
import inu.market.category.dto.CategoryResponse;
import inu.market.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/api/categories")
    public ResponseEntity<CategoryResponse> create(@RequestBody @Valid CategoryCreateRequest request) {
        return ResponseEntity.ok(categoryService.create(request));
    }

}
