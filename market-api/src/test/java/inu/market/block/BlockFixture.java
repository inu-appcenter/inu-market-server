package inu.market.block;

import inu.market.block.domain.Block;
import inu.market.block.dto.BlockCreateRequest;
import inu.market.block.dto.BlockResponse;

import static inu.market.user.UserFixture.TEST_USER;
import static inu.market.user.UserFixture.TEST_USER1;

public class BlockFixture {

    public static final Block TEST_BLOCK = new Block(1L, TEST_USER, TEST_USER1);

    public static final BlockCreateRequest TEST_BLOCK_CREATE_REQUEST
            = new BlockCreateRequest(TEST_USER.getId());

    public static final BlockResponse TEST_BLOCK_RESPONSE = BlockResponse.from(TEST_BLOCK);
}
