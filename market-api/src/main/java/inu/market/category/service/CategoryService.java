package inu.market.category.service;

import inu.market.category.domain.Category;
import inu.market.category.domain.CategoryRepository;
import inu.market.category.dto.CategoryCreateRequest;
import inu.market.category.dto.CategoryResponse;
import inu.market.category.dto.CategoryUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponse create(CategoryCreateRequest request) {

        if (categoryRepository.findByName(request.getName()).isPresent()) {
            throw new RuntimeException(request.getName() + "는 이미 존재하는 카테고리입니다.");
        }

        Category category = Category.createCategory(request.getName());
        categoryRepository.save(category);

        return CategoryResponse.from(category);
    }

    @Transactional
    public CategoryResponse update(Long categoryId, CategoryUpdateRequest request) {

        Category findCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException(categoryId + "는 존재하지 않는 카테고리 ID 입니다."));

        if(categoryRepository.findByName(request.getName()).isPresent()){
            throw new RuntimeException(request.getName() + "는 이미 존재하는 카테고리입니다.");
        }

        findCategory.changeName(request.getName());
        return CategoryResponse.from(findCategory);
    }

    @Transactional
    public void delete(Long categoryId) {

        Category findCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException(categoryId + "는 존재하지 않는 카테고리 ID 입니다."));

        categoryRepository.delete(findCategory);
    }

    public List<CategoryResponse> findAll() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> CategoryResponse.from(category))
                .collect(Collectors.toList());
    }
}
