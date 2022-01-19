package inu.market.favorite.service;

import inu.market.common.NotFoundException;
import inu.market.favorite.domain.Favorite;
import inu.market.favorite.domain.FavoriteRepository;
import inu.market.favorite.dto.FavoriteCreateRequest;
import inu.market.favorite.dto.FavoriteDeleteRequest;
import inu.market.item.domain.Item;
import inu.market.item.domain.ItemRepository;
import inu.market.item.dto.ItemResponse;
import inu.market.item.query.ItemQueryRepository;
import inu.market.notification.domain.Notification;
import inu.market.notification.domain.NotificationRepository;
import inu.market.notification.domain.NotificationType;
import inu.market.user.domain.User;
import inu.market.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final NotificationRepository notificationRepository;
    private final ItemQueryRepository itemQueryRepository;

    @Transactional
    public void create(Long userId, FavoriteCreateRequest request) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(userId + "는 존재하지 않는 회원 ID 입니다."));

        Item findItem = itemRepository.findWithSellerById(request.getItemId())
                .orElseThrow(() -> new NotFoundException(request.getItemId() + "는 존재하지 않는 상품 ID 입니다."));
        User seller = findItem.getSeller();

        Favorite favorite = Favorite.createFavorite(findUser, findItem);
        favoriteRepository.save(favorite);

        Notification notification = Notification
                .createNotification(makeFavoriteMessage(findUser.getNickName(), findItem.getTitle()), NotificationType.FAVORITE, findItem.getId(), seller);
        notificationRepository.save(notification.create());
    }

    @Transactional
    public void delete(Long userId, FavoriteDeleteRequest request) {
        Favorite findFavorite = favoriteRepository.findWithItemByUserIdAndItemId(userId, request.getItemId())
                .orElseThrow(() -> new NotFoundException("찜 목록에 존재하지 않습니다."));

        findFavorite.getItem().decreaseFavoriteCount();
        favoriteRepository.delete(findFavorite);
    }

    public List<ItemResponse> findByUserId(Long userId) {
        return itemQueryRepository.findByFavoriteUserId(userId);
    }

    private String makeFavoriteMessage(String nickName, String title) {
        return nickName + "님이 " + title + "를 찜 목록에 추가했습니다.";
    }
}
