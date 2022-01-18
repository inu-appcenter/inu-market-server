package inu.market.category;

import inu.market.category.domain.Category;
import inu.market.category.dto.CategoryCreateRequest;
import inu.market.category.dto.CategoryResponse;
import inu.market.category.dto.CategoryUpdateRequest;

public class CategoryFixture {

    public static final String TEST_CATEGORY_NAME = "전자기기";
    public static final String TEST_CATEGORY_ICON_URL = "아이콘 URL";

    public static final CategoryCreateRequest TEST_CATEGORY_CREATE_REQUEST
            = new CategoryCreateRequest(TEST_CATEGORY_NAME, TEST_CATEGORY_ICON_URL);

    public static final CategoryUpdateRequest TEST_CATEGORY_UPDATE_REQUEST
            = new CategoryUpdateRequest(TEST_CATEGORY_NAME, TEST_CATEGORY_ICON_URL);

    public static final Category TEST_CATEGORY
            = new Category(1L, TEST_CATEGORY_NAME, TEST_CATEGORY_ICON_URL);

    public static final CategoryResponse TEST_CATEGORY_RESPONSE
            = CategoryResponse.from(TEST_CATEGORY);
}
