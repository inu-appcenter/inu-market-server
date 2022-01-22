package inu.market.category.service;

import inu.market.category.domain.Category;
import inu.market.category.domain.CategoryRepository;
import inu.market.category.dto.CategoryCreateRequest;
import inu.market.category.dto.CategoryResponse;
import inu.market.category.dto.CategoryUpdateRequest;
import inu.market.client.AwsClient;
import inu.market.common.DuplicateException;
import inu.market.common.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static inu.market.common.NotFoundException.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final AwsClient awsClient;

    @Transactional
    public Long create(CategoryCreateRequest request) {
        if (categoryRepository.findByName(request.getName()).isPresent()) {
            throw new DuplicateException(String.format("%s는 이미 존재하는 카테고리입니다.", request.getName()));
        }

        Category category = Category.createCategory(request.getName(), request.getIconUrl());
        return categoryRepository.save(category).getId();
    }

    @Transactional
    public void update(Long categoryId, CategoryUpdateRequest request) {
        Category findCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND));

        if (categoryRepository.findByName(request.getName()).isPresent()) {
            throw new DuplicateException(String.format("%s는 이미 존재하는 카테고리입니다.", request.getName()));
        }

        findCategory.changeNameAndIconUrl(request.getName(), request.getIconUrl());
    }

    @Transactional
    public void delete(Long categoryId) {
        Category findCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND));

        categoryRepository.delete(findCategory);
    }

    public List<CategoryResponse> findAll() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> CategoryResponse.from(category))
                .collect(Collectors.toList());
    }

    public String uploadImage(MultipartFile image) {
        return awsClient.upload(image);
    }
}
