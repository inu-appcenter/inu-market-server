package inu.market.major.service;

import inu.market.common.DuplicateException;
import inu.market.major.domain.MajorRepository;
import inu.market.major.dto.MajorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static inu.market.major.MajorFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MajorServiceTest {

    @InjectMocks
    private MajorService majorService;

    @Mock
    private MajorRepository majorRepository;

    @Test
    @DisplayName("단과 대학을 생성한다.")
    void createParent() {
        // given
        given(majorRepository.findByName(any()))
                .willReturn(Optional.empty());

        given(majorRepository.save(any()))
                .willReturn(TEST_MAJOR);
        // when
        Long result = majorService.createParent(TEST_MAJOR_CREATE_REQUEST);

        // then
        assertThat(result).isEqualTo(TEST_MAJOR.getId());
        then(majorRepository).should(times(1)).findByName(any());
        then(majorRepository).should(times(1)).save(any());
    }

    @Test
    @DisplayName("중복된 단과 대학을 생성하면 예외가 발생한다.")
    void createDuplicateParent() {
        // given
        given(majorRepository.findByName(any()))
                .willReturn(Optional.of(TEST_MAJOR));

        // when
        assertThrows(DuplicateException.class, () -> majorService.createParent(TEST_MAJOR_CREATE_REQUEST));

        // then
        then(majorRepository).should(times(1)).findByName(any());
    }

    @Test
    @DisplayName("학과를 생성한다.")
    void createChildren() {
        // given
        given(majorRepository.findById(any()))
                .willReturn(Optional.of(TEST_PARENT_MAJOR));

        given(majorRepository.findByName(any()))
                .willReturn(Optional.empty());

        given(majorRepository.save(any()))
                .willReturn(TEST_MAJOR);
        // when
        Long result = majorService.createChildren(TEST_PARENT_MAJOR.getId(), TEST_MAJOR_CREATE_REQUEST);

        // then
        assertThat(result).isEqualTo(TEST_MAJOR.getId());
        then(majorRepository).should(times(1)).findById(any());
        then(majorRepository).should(times(1)).findByName(any());
        then(majorRepository).should(times(1)).save(any());
    }

    @Test
    @DisplayName("중복된 학과를 생성하면 예외가 발생한다.")
    void createDuplicateChildren() {
        // given
        given(majorRepository.findById(any()))
                .willReturn(Optional.of(TEST_PARENT_MAJOR));

        given(majorRepository.findByName(any()))
                .willReturn(Optional.of(TEST_MAJOR));
        // when
        assertThrows(DuplicateException.class, () -> majorService.createChildren(TEST_PARENT_MAJOR.getId(), TEST_MAJOR_CREATE_REQUEST));

        // then
        then(majorRepository).should(times(1)).findById(any());
        then(majorRepository).should(times(1)).findByName(any());
    }

    @Test
    @DisplayName("학과를 수정한다.")
    void update() {
        // given
        given(majorRepository.findById(any()))
                .willReturn(Optional.of(TEST_MAJOR));

        given(majorRepository.findByName(any()))
                .willReturn(Optional.empty());

        // when
        majorService.update(TEST_MAJOR.getId(), TEST_MAJOR_UPDATE_REQUEST);

        // then
        assertThat(TEST_MAJOR.getName()).isEqualTo(TEST_MAJOR_UPDATE_REQUEST.getName());
        then(majorRepository).should(times(1)).findById(any());
        then(majorRepository).should(times(1)).findByName(any());
    }

    @Test
    @DisplayName("중복된 학과로 수정하면 예외가 발생한다.")
    void updateDuplicate() {
        // given
        given(majorRepository.findById(any()))
                .willReturn(Optional.of(TEST_MAJOR));

        given(majorRepository.findByName(any()))
                .willReturn(Optional.of(TEST_MAJOR));

        // when
        assertThrows(DuplicateException.class, () -> majorService.update(TEST_MAJOR.getId(), TEST_MAJOR_UPDATE_REQUEST));

        // then
        then(majorRepository).should(times(1)).findById(any());
        then(majorRepository).should(times(1)).findByName(any());
    }

    @Test
    @DisplayName("학과를 삭제한다.")
    void delete() {
        // given
        given(majorRepository.findById(any()))
                .willReturn(Optional.of(TEST_MAJOR));

        willDoNothing()
                .given(majorRepository)
                .delete(any());
        // when
        majorService.delete(TEST_MAJOR.getId());

        // then
        then(majorRepository).should(times(1)).findById(any());
        then(majorRepository).should(times(1)).delete(any());
    }

    @Test
    @DisplayName("단과대학을 조회한다.")
    void findParents() {
        // given
        given(majorRepository.findByParentIsNull())
                .willReturn(Arrays.asList(TEST_PARENT_MAJOR));

        // when
        List<MajorResponse> result = majorService.findParents();

        // then
        assertThat(result.size()).isEqualTo(1);
        then(majorRepository).should(times(1)).findByParentIsNull();
    }

    @Test
    @DisplayName("하위 학과를 조회한다.")
    void findChildrenById() {
        // given
        given(majorRepository.findByParentId(any()))
                .willReturn(Arrays.asList(TEST_MAJOR));

        // when
        List<MajorResponse> result = majorService.findChildrenById(TEST_PARENT_MAJOR.getId());

        // then
        assertThat(result.size()).isEqualTo(1);
        then(majorRepository).should(times(1)).findByParentId(any());
    }
}