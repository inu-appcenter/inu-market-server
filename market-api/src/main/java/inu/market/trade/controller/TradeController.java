package inu.market.trade.controller;

import inu.market.trade.dto.TradeCreateRequest;
import inu.market.trade.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    @PostMapping("/api/trades")
    public ResponseEntity<Void> create(@RequestBody @Valid TradeCreateRequest request) {
        tradeService.create(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
