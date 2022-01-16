package inu.market.trade.service;

import inu.market.client.FirebaseClient;
import inu.market.item.domain.Item;
import inu.market.item.domain.ItemRepository;
import inu.market.item.dto.ItemResponse;
import inu.market.notification.domain.Notification;
import inu.market.notification.domain.NotificationRepository;
import inu.market.notification.domain.NotificationType;
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
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TradeService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final TradeRepository tradeRepository;
    private final FirebaseClient firebaseClient;
    private final NotificationRepository notificationRepository;

    @Transactional
    public void create(TradeCreateRequest request) {
        Item findItem = itemRepository.findWithSellerById(request.getItemId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 상품입니다."));

        User findUser = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        Trade trade = Trade.createTrade(findItem, findUser);
        tradeRepository.save(trade);

        Notification notification = Notification
                .createNotification(makeTradeMessage(findUser.getNickName()), NotificationType.TRADE, findItem.getId(), findUser);
        notificationRepository.save(notification);

        firebaseClient.send(findUser.getPushToken(), "INOM", notification.getContent());
    }


    public List<ItemResponse> findByBuyerId(Long userId) {
        List<Trade> trades = tradeRepository.findWithItemByBuyerId(userId);
        return trades.stream()
                .map(trade -> ItemResponse.from(trade.getItem()))
                .collect(Collectors.toList());
    }

    private String makeTradeMessage(String nickName) {
        return nickName + "님과의 거래가 완료되었습니다.";
    }

}
