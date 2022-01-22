package inu.market.notice.service;

import inu.market.common.NotFoundException;
import inu.market.notice.domain.NoticeRepository;
import inu.market.notice.dto.NoticeResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static inu.market.notice.NoticeFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class NoticeServiceTest {

    @InjectMocks
    private NoticeService noticeService;

    @Mock
    private NoticeRepository noticeRepository;

    @Test
    @DisplayName("공지사항을 생성한다.")
    void create() {
        // given
        given(noticeRepository.save(any()))
                .willReturn(TEST_NOTICE);

        // when
        Long result = noticeService.create(TEST_NOTICE_CREATE_REQUEST);

        // then
        assertThat(result).isEqualTo(TEST_NOTICE.getId());

        then(noticeRepository).should(times(1)).save(any());
    }

    @Test
    @DisplayName("공지사항을 수정한다.")
    void update() {
        // given
        given(noticeRepository.findById(any()))
                .willReturn(Optional.of(TEST_NOTICE));

        // when
        noticeService.update(TEST_NOTICE.getId(), TEST_NOTICE_UPDATE_REQUEST);

        // then
        then(noticeRepository).should(times(1)).findById(any());
    }

    @Test
    @DisplayName("공지사항을 수정할 때 공지사항이 존재하지 않으면 예외가 발생한다..")
    void updateNotFound() {
        // given
        given(noticeRepository.findById(any()))
                .willReturn(Optional.empty());

        // when
        assertThrows(NotFoundException.class, () -> noticeService.update(TEST_NOTICE.getId(), TEST_NOTICE_UPDATE_REQUEST));

        // then
        then(noticeRepository).should(times(1)).findById(any());
    }

    @Test
    @DisplayName("공지사항을 삭제한다.")
    void delete() {
        // given
        given(noticeRepository.findById(any()))
                .willReturn(Optional.of(TEST_NOTICE));

        // when
        noticeService.delete(TEST_NOTICE.getId());

        // then
        then(noticeRepository).should(times(1)).findById(any());
    }

    @Test
    @DisplayName("공지사항을 삭제할 때 공지사항이 존재하지 않으면 예외가 발생한다..")
    void deleteNotFound() {
        // given
        given(noticeRepository.findById(any()))
                .willReturn(Optional.empty());

        // when
        assertThrows(NotFoundException.class, () -> noticeService.delete(TEST_NOTICE.getId()));

        // then
        then(noticeRepository).should(times(1)).findById(any());
    }

    @Test
    @DisplayName("공지사항 리스트를 조회한다.")
    void findAll() {
        // given
        given(noticeRepository.findAll())
                .willReturn(Arrays.asList(TEST_NOTICE));

        // when
        List<NoticeResponse> result = noticeService.findAll();

        // then
        assertThat(result.size()).isEqualTo(1);

        then(noticeRepository).should(times(1)).findAll();
    }

    @Test
    @DisplayName("공지사항을 상세조회한다.")
    void findById() {
        // given
        given(noticeRepository.findById(any()))
                .willReturn(Optional.of(TEST_NOTICE));

        // when
        NoticeResponse result = noticeService.findById(TEST_NOTICE.getId());

        // then
        assertThat(result.getTitle()).isEqualTo(TEST_NOTICE.getTitle());
        assertThat(result.getContent()).isEqualTo(TEST_NOTICE.getContent());

        then(noticeRepository).should(times(1)).findById(any());
    }

    @Test
    @DisplayName("공지사항을 상세조회할 때 공지사항이 존재하지 않으면 예외가 발생한다.")
    void findByIdNotFound() {
        // given
        given(noticeRepository.findById(any()))
                .willReturn(Optional.empty());

        // when
        assertThrows(NotFoundException.class, () -> noticeService.findById(TEST_NOTICE.getId()));

        // then
        then(noticeRepository).should(times(1)).findById(any());
    }
}