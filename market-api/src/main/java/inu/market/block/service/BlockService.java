package inu.market.block.service;

import inu.market.block.domain.Block;
import inu.market.block.domain.BlockRepository;
import inu.market.block.dto.BlockCreateRequest;
import inu.market.block.dto.BlockResponse;
import inu.market.common.DuplicateException;
import inu.market.common.NotFoundException;
import inu.market.user.domain.User;
import inu.market.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static inu.market.common.NotFoundException.BLOCK_NOT_FOUND;
import static inu.market.common.NotFoundException.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlockService {

    private final BlockRepository blockRepository;
    private final UserRepository userRepository;

    @Transactional
    public void create(Long userId, BlockCreateRequest request) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        User targetUser = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        if (blockRepository.findByUserAndTarget(findUser, targetUser).isPresent()) {
            throw new DuplicateException("이미 차단한 회원입니다.");
        }

        Block block = Block.createBlock(findUser, targetUser);
        blockRepository.save(block);
    }

    @Transactional
    public void delete(Long userId, Long blockId) {
        Block findBlock = blockRepository.findById(blockId)
                .orElseThrow(() -> new NotFoundException(BLOCK_NOT_FOUND));

        if(!findBlock.getUser().getId().equals(userId)){
            throw new AccessDeniedException("권한이 없습니다.");
        }

        blockRepository.delete(findBlock);
    }

    public List<BlockResponse> findByUserId(Long userId) {
        List<Block> blocks = blockRepository.findByUserId(userId);
        return blocks.stream()
                .map(BlockResponse::from)
                .collect(Collectors.toList());
    }
}
