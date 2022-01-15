package inu.market.favorite.service;

import inu.market.favorite.domain.Favorite;
import inu.market.favorite.domain.FavoriteRepository;
import inu.market.favorite.dto.FavoriteCreateRequest;
import inu.market.favorite.dto.FavoriteDeleteRequest;
import inu.market.item.domain.Item;
import inu.market.item.domain.ItemRepository;
import inu.market.item.dto.ItemResponse;
import inu.market.user.domain.User;
import inu.market.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public void create(Long userId, FavoriteCreateRequest request) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        Item findItem = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 상품입니다."));

        Favorite favorite = Favorite.createFavorite(findUser, findItem);
        favoriteRepository.save(favorite);
    }

    @Transactional
    public void delete(Long userId, FavoriteDeleteRequest request) {
        Favorite findFavorite = favoriteRepository.findWithItemByUserIdAndItemId(userId, request.getItemId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 찜입니다."));

        findFavorite.getItem().decreaseFavoriteCount();
        favoriteRepository.delete(findFavorite);
    }

    public List<ItemResponse> findByUserId(Long userId) {
        List<Favorite> favorites = favoriteRepository.findWithItemByUserId(userId);
        return favorites.stream()
                .map(favorite -> ItemResponse.from(favorite.getItem()))
                .collect(Collectors.toList());
    }
}
