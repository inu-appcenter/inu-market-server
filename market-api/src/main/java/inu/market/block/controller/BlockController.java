package inu.market.block.controller;

import inu.market.block.dto.BlockCreateRequest;
import inu.market.block.service.BlockService;
import inu.market.config.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class BlockController {

    private final BlockService blockService;

    @PostMapping("/api/blocks")
    public ResponseEntity<Void> create(@LoginUser Long userId,
                                       @RequestBody @Valid BlockCreateRequest request) {
        blockService.create(userId, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/api/blocks/{blockId}")
    public ResponseEntity<Void> delete(@LoginUser Long userId,
                                       @PathVariable Long blockId) {
        blockService.delete(userId, blockId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
