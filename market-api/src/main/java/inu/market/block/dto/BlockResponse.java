package inu.market.block.dto;

import inu.market.block.domain.Block;
import inu.market.user.domain.User;
import inu.market.user.dto.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlockResponse {

    private Long blockId;

    private UserResponse target;

    public static BlockResponse from(Block block) {
        return new BlockResponse(block.getId(), UserResponse.from(block.getTarget()));
    }

}
