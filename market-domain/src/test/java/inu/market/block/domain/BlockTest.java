package inu.market.block.domain;

import inu.market.user.domain.Role;
import inu.market.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BlockTest {

    @Test
    @DisplayName("차단을 생성한다.")
    void createBlock() {
        // given
        User user = User.createUser(201601757, Role.ROLE_USER);
        User target = User.createUser(201601758, Role.ROLE_USER);

        // when
        Block result = Block.createBlock(user, target);

        // then
        assertThat(result.getUser()).isEqualTo(user);
        assertThat(result.getTarget()).isEqualTo(target);
    }
}