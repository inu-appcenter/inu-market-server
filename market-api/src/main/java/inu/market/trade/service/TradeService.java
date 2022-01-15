package inu.market.trade.service;

import inu.market.item.domain.Item;
import inu.market.item.domain.ItemRepository;
import inu.market.trade.domain.Trade;
import inu.market.trade.domain.TradeRepository;
import inu.market.trade.dto.TradeCreateRequest;
import inu.market.user.domain.User;
import inu.market.user.domain.UserRepository;
import inu.market.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TradeService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final TradeRepository tradeRepository;

    @Transactional
    public void create(TradeCreateRequest request) {
        Item findItem = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 상품입니다."));

        User findUser = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        Trade trade = Trade.createTrade(findItem, findUser);
        tradeRepository.save(trade);
    }
}
