package inu.market.block.service;

import inu.market.block.domain.BlockRepository;
import inu.market.block.dto.BlockResponse;
import inu.market.common.DuplicateException;
import inu.market.common.NotFoundException;
import inu.market.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static inu.market.block.BlockFixture.TEST_BLOCK;
import static inu.market.block.BlockFixture.TEST_BLOCK_CREATE_REQUEST;
import static inu.market.user.UserFixture.TEST_USER;
import static inu.market.user.UserFixture.TEST_USER1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class BlockServiceTest {

    @InjectMocks
    private BlockService blockService;

    @Mock
    private BlockRepository blockRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("차단을 생성한다.")
    void create() {
        // given
        given(userRepository.findById(any()))
                .willReturn(Optional.of(TEST_USER))
                .willReturn(Optional.of(TEST_USER));

        given(blockRepository.findByUserAndTarget(any(), any()))
                .willReturn(Optional.empty());

        given(blockRepository.save(any()))
                .willReturn(TEST_BLOCK);

        // when
        blockService.create(TEST_USER.getId(), TEST_BLOCK_CREATE_REQUEST);

        // then
        then(userRepository).should(times(2)).findById(any());
        then(blockRepository).should(times(1)).findByUserAndTarget(any(), any());
        then(blockRepository).should(times(1)).save(any());
    }

    @Test
    @DisplayName("차단을 생성할 때 회원이 존재하지 않으면 예외가 발생한다.")
    void createNotFoundUser() {
        // given
        given(userRepository.findById(any()))
                .willReturn(Optional.empty());

        // when
        assertThrows(NotFoundException.class, () -> blockService.create(TEST_USER.getId(), TEST_BLOCK_CREATE_REQUEST));

        // then
        then(userRepository).should(times(1)).findById(any());
    }

    @Test
    @DisplayName("차단을 생성할 때 타겟이 존재하지 않으면 예외가 발생한다.")
    void createNotFoundTarget() {
        // given
        given(userRepository.findById(any()))
                .willReturn(Optional.of(TEST_USER))
                .willReturn(Optional.empty());

        // when
        assertThrows(NotFoundException.class, () -> blockService.create(TEST_USER.getId(), TEST_BLOCK_CREATE_REQUEST));

        // then
        then(userRepository).should(times(2)).findById(any());
    }

    @Test
    @DisplayName("중복 차단을 생성하면 예외가 발생한다.")
    void createDuplicate() {
        // given
        given(userRepository.findById(any()))
                .willReturn(Optional.of(TEST_USER));

        given(userRepository.findById(any()))
                .willReturn(Optional.of(TEST_USER));

        given(blockRepository.findByUserAndTarget(any(), any()))
                .willReturn(Optional.of(TEST_BLOCK));

        // when
        assertThrows(DuplicateException.class, () -> blockService.create(TEST_USER.getId(), TEST_BLOCK_CREATE_REQUEST));

        // then
        then(userRepository).should(times(2)).findById(any());
        then(blockRepository).should(times(1)).findByUserAndTarget(any(), any());
    }

    @Test
    @DisplayName("차단을 해제한다.")
    void delete() {
        // given
        given(blockRepository.findById(any()))
                .willReturn(Optional.of(TEST_BLOCK));

        // when
        blockService.delete(TEST_USER.getId(), TEST_BLOCK.getId());

        // then
        then(blockRepository).should(times(1)).findById(any());
    }

    @Test
    @DisplayName("차단을 해제할 때 차단이 존재하지 않으면 예외가 발생한다.")
    void deleteNotFound() {
        // given
        given(blockRepository.findById(any()))
                .willReturn(Optional.empty());

        // when
        assertThrows(NotFoundException.class, () -> blockService.delete(TEST_USER.getId(), TEST_BLOCK.getId()));

        // then
        then(blockRepository).should(times(1)).findById(any());
    }

    @Test
    @DisplayName("차단을 한 회원이 아니면 차단 해제시 예외가 발생한다.")
    void deleteNotOwner() {
        // given
        given(blockRepository.findById(any()))
                .willReturn(Optional.of(TEST_BLOCK));

        // when
        assertThrows(AccessDeniedException.class, () -> blockService.delete(TEST_USER1.getId(), TEST_BLOCK.getId()));
    }

    @Test
    @DisplayName("차단 회원 리스트를 조회한다.")
    void findByUserId() {
        // given
        given(blockRepository.findByUserId(any()))
                .willReturn(Arrays.asList(TEST_BLOCK));

        // when
        List<BlockResponse> result = blockService.findByUserId(TEST_USER.getId());

        // then
        assertThat(result.size()).isEqualTo(1);
    }
}