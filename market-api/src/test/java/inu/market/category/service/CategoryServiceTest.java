package inu.market.category.service;

import inu.market.category.domain.CategoryRepository;
import inu.market.category.dto.CategoryResponse;
import inu.market.client.AwsClient;
import inu.market.common.DuplicateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static inu.market.CommonFixture.TEST_IMAGE_FILE;
import static inu.market.category.CategoryFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private AwsClient awsClient;

    @Test
    @DisplayName("카테고리를 생성한다.")
    void create() {
        // given
        given(categoryRepository.findByName(any()))
                .willReturn(Optional.empty());

        given(categoryRepository.save(any()))
                .willReturn(TEST_CATEGORY);

        // when
        Long result = categoryService.create(TEST_CATEGORY_CREATE_REQUEST);

        // then
        assertThat(result).isEqualTo(TEST_CATEGORY.getId());

        then(categoryRepository).should(times(1)).findByName(any());
        then(categoryRepository).should(times(1)).save(any());
    }

    @Test
    @DisplayName("중복된 카테고리를 생성하면 예외가 발생한다.")
    void createDuplicate() {
        // given
        given(categoryRepository.findByName(any()))
                .willReturn(Optional.of(TEST_CATEGORY));

        // when
        assertThrows(DuplicateException.class, () -> categoryService.create(TEST_CATEGORY_CREATE_REQUEST));

        // then
        then(categoryRepository).should(times(1)).findByName(any());
    }


    @Test
    @DisplayName("카테고리를 수정한다.")
    void update() {
        // given
        given(categoryRepository.findById(any()))
                .willReturn(Optional.of(TEST_CATEGORY));

        given((categoryRepository.findByName(any())))
                .willReturn(Optional.empty());

        // when
        categoryService.update(TEST_CATEGORY.getId(), TEST_CATEGORY_UPDATE_REQUEST);

        // then
        assertThat(TEST_CATEGORY.getName()).isEqualTo(TEST_CATEGORY_UPDATE_REQUEST.getName());
        assertThat(TEST_CATEGORY.getIconUrl()).isEqualTo(TEST_CATEGORY_UPDATE_REQUEST.getIconUrl());

        then(categoryRepository).should(times(1)).findById(any());
        then(categoryRepository).should(times(1)).findByName(any());
    }

    @Test
    @DisplayName("중복된 카테고리로 수정하면 예외가 발생한다.")
    void updateDuplicate() {
        // given
        given(categoryRepository.findById(any()))
                .willReturn(Optional.of(TEST_CATEGORY));

        given((categoryRepository.findByName(any())))
                .willReturn(Optional.of(TEST_CATEGORY));

        // when
        assertThrows(DuplicateException.class, () -> categoryService.update(TEST_CATEGORY.getId(), TEST_CATEGORY_UPDATE_REQUEST));

        // then
        then(categoryRepository).should(times(1)).findById(any());
        then(categoryRepository).should(times(1)).findByName(any());
    }

    @Test
    @DisplayName("카테고리를 삭제한다.")
    void delete() {
        // given
        given(categoryRepository.findById(any()))
                .willReturn(Optional.of(TEST_CATEGORY));

        willDoNothing()
                .given(categoryRepository)
                .delete(any());
        // when
        categoryService.delete(TEST_CATEGORY.getId());

        // then
        then(categoryRepository).should(times(1)).findById(any());
        then(categoryRepository).should(times(1)).delete(any());
    }

    @Test
    @DisplayName("카테고리 리스트를 조회한다.")
    void findAll() {
        // given
        given(categoryRepository.findAll())
                .willReturn(Arrays.asList(TEST_CATEGORY));
        // when
        List<CategoryResponse> result = categoryService.findAll();

        // then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("이미지를 업로드한다.")
    void uploadImage() {
        // given
        given(awsClient.upload(any()))
                .willReturn(TEST_CATEGORY_ICON_URL);

        // when
        String result = categoryService.uploadImage(TEST_IMAGE_FILE);

        // then
        assertThat(result).isEqualTo(TEST_CATEGORY_ICON_URL);
        then(awsClient).should(times(1)).upload(any());
    }
}