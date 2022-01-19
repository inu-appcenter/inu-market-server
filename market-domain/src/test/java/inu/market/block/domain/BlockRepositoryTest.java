package inu.market.block.domain;

import inu.market.user.domain.Role;
import inu.market.user.domain.User;
import inu.market.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class BlockRepositoryTest {

    @Autowired
    private BlockRepository blockRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        blockRepository.deleteAll();
    }

    @Test
    @DisplayName("회원과 타겟 회원으로 차단을 조회한다.")
    void findByUserAndTarget() {
        // given
        User user = User.createUser(201601757, Role.ROLE_USER);
        User target = User.createUser(201601757, Role.ROLE_USER);
        Block block = Block.createBlock(user, target);

        userRepository.save(user);
        userRepository.save(target);
        blockRepository.save(block);

        // when
        Block result = blockRepository.findByUserAndTarget(user, target).get();

        // then
        assertThat(result).isEqualTo(block);
    }

    @Test
    @DisplayName("회원의 차단 목록을 조회한다.")
    void findByUserId() {
        // given
        User user = User.createUser(201601757, Role.ROLE_USER);
        User target = User.createUser(201601757, Role.ROLE_USER);
        Block block = Block.createBlock(user, target);

        userRepository.save(user);
        userRepository.save(target);
        blockRepository.save(block);

        // when
        List<Block> result = blockRepository.findByUserId(user.getId());

        // then
        assertThat(result.size()).isEqualTo(1);
    }
}