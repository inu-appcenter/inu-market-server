package inu.market.block.service;

import inu.market.block.domain.Block;
import inu.market.block.domain.BlockRepository;
import inu.market.block.dto.BlockCreateRequest;
import inu.market.user.domain.User;
import inu.market.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlockService {

    private final BlockRepository blockRepository;
    private final UserRepository userRepository;

    @Transactional
    public void create(Long userId, BlockCreateRequest request) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        User targetUser = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        if (blockRepository.findByUserAndTarget(findUser, targetUser).isPresent()) {
            throw new RuntimeException("이미 차단한 회원입니다.");
        }

        Block block = Block.createBlock(findUser, targetUser);
        blockRepository.save(block);
    }

    @Transactional
    public void delete(Long userId, Long blockId) {
        Block findBlock = blockRepository.findById(blockId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 차단입니다."));

        if(!findBlock.getUser().getId().equals(userId)){
            throw new RuntimeException("권한이 없습니다.");
        }

        blockRepository.delete(findBlock);
    }

}
