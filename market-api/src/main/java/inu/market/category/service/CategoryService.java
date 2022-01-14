package inu.market.category.service;

import inu.market.category.domain.Category;
import inu.market.category.domain.CategoryRepository;
import inu.market.category.dto.CategoryCreateRequest;
import inu.market.category.dto.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        return CategoryResponse.of(category);
    }

}
