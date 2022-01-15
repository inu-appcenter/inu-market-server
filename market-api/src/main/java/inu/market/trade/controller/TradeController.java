package inu.market.trade.controller;

import inu.market.config.LoginUser;
import inu.market.item.dto.ItemResponse;
import inu.market.trade.dto.TradeCreateRequest;
import inu.market.trade.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    @PostMapping("/api/trades")
    public ResponseEntity<Void> create(@RequestBody @Valid TradeCreateRequest request) {
        tradeService.create(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/api/trades")
    public ResponseEntity<List<ItemResponse>> findByBuyerId(@LoginUser Long userId){
        return ResponseEntity.ok(tradeService.findByBuyerId(userId));
    }

}
