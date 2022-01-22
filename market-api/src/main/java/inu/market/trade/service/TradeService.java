package inu.market.trade.service;

import inu.market.common.NotFoundException;
import inu.market.item.domain.Item;
import inu.market.item.domain.ItemRepository;
import inu.market.item.dto.ItemResponse;
import inu.market.item.query.ItemQueryRepository;
import inu.market.notification.domain.Notification;
import inu.market.notification.domain.NotificationRepository;
import inu.market.notification.domain.NotificationType;
import inu.market.trade.domain.Trade;
import inu.market.trade.domain.TradeRepository;
import inu.market.trade.dto.TradeCreateRequest;
import inu.market.user.domain.User;
import inu.market.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static inu.market.common.NotFoundException.ITEM_NOT_FOUND;
import static inu.market.common.NotFoundException.USER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TradeService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final TradeRepository tradeRepository;
    private final NotificationRepository notificationRepository;
    private final ItemQueryRepository itemQueryRepository;

    @Transactional
    public void create(Long userId, TradeCreateRequest request) {
        Item findItem = itemRepository.findWithSellerById(request.getItemId())
                .orElseThrow(() -> new NotFoundException(ITEM_NOT_FOUND));

        if (!findItem.getSeller().getId().equals(userId)) {
            throw new AccessDeniedException("");
        }

        User findUser = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        Trade trade = Trade.createTrade(findItem, findUser);
        tradeRepository.save(trade);

        Notification notification = Notification
                .createNotification(makeTradeMessage(findUser.getNickName()), NotificationType.TRADE, findItem.getId(), findUser);
        notificationRepository.save(notification.create());
    }

    public List<ItemResponse> findByBuyerId(Long userId) {
        return itemQueryRepository.findByTradeBuyerId(userId);
    }

    private String makeTradeMessage(String nickName) {
        return String.format("%s님과 거래가 완료되었습니다.", nickName);
    }

}
