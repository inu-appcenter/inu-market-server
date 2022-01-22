package inu.market.item.service;

import inu.market.category.domain.Category;
import inu.market.category.domain.CategoryRepository;
import inu.market.chatroom.domain.ChatRoomRepository;
import inu.market.client.AwsClient;
import inu.market.common.NotFoundException;
import inu.market.favorite.domain.Favorite;
import inu.market.favorite.domain.FavoriteRepository;
import inu.market.item.domain.Item;
import inu.market.item.query.ItemQueryRepository;
import inu.market.item.domain.ItemRepository;
import inu.market.item.domain.Status;
import inu.market.item.dto.*;
import inu.market.major.domain.Major;
import inu.market.major.domain.MajorRepository;
import inu.market.trade.domain.TradeRepository;
import inu.market.user.domain.User;
import inu.market.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static inu.market.common.NotFoundException.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final MajorRepository majorRepository;
    private final FavoriteRepository favoriteRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ItemQueryRepository itemQueryRepository;
    private final TradeRepository tradeRepository;

    private final AwsClient awsClient;

    public List<String> uploadImages(List<MultipartFile> images) {
        return images.stream()
                .map(image -> awsClient.upload(image))
                .collect(Collectors.toList());
    }

    @Transactional
    public Long create(Long userId, ItemCreateRequest request) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        Category findCategory = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND));

        Major findMajor = majorRepository.findById(request.getMajorId())
                .orElseThrow(() -> new NotFoundException(MAJOR_NOT_FOUND));

        Item item = Item.createItem(request.getTitle(), request.getContents(), request.getImageUrls().get(0),
                request.getPrice(), Status.SALE, findUser);

        item.changeCategory(findCategory);
        item.changeMajor(findMajor);
        item.changeItemImages(request.getImageUrls());

        return itemRepository.save(item).getId();
    }

    @Transactional
    public void update(Long userId, Long itemId, ItemUpdateRequest request) {
        Item findItem = itemRepository.findWithItemImagesById(itemId)
                .orElseThrow(() -> new NotFoundException(ITEM_NOT_FOUND));

        if (!findItem.getSeller().getId().equals(userId)) {
            throw new AccessDeniedException("");
        }

        Category findCategory = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND));

        Major findMajor = majorRepository.findById(request.getMajorId())
                .orElseThrow(() -> new NotFoundException(MAJOR_NOT_FOUND));

        findItem.changeTitleAndContentAndPrice(request.getTitle(), request.getContents(), request.getPrice());
        findItem.changeCategory(findCategory);
        findItem.changeMajor(findMajor);
        findItem.changeItemImages(request.getImageUrls());
    }

    @Transactional
    public void updateStatus(Long userId, Long itemId, ItemUpdateStatusRequest request) {
        Item findItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(ITEM_NOT_FOUND));

        if (!findItem.getSeller().getId().equals(userId)) {
            throw new AccessDeniedException("");
        }

        findItem.changeStatus(Status.from(request.getStatus()));
    }

    @Transactional
    public void delete(Long userId, Long itemId) {
        Item findItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(ITEM_NOT_FOUND));

        if (!findItem.getSeller().getId().equals(userId)) {
            throw new AccessDeniedException("");
        }

        tradeRepository.deleteAllByItem(findItem);
        favoriteRepository.deleteAllByItem(findItem);
        chatRoomRepository.deleteAllByItem(findItem);
        itemRepository.delete(findItem);
    }

    public ItemResponse findById(Long userId, Long itemId) {
        Item findItem = itemRepository.findWithSellerAndItemImagesAndCategoryAndMajorById(itemId)
                .orElseThrow(() -> new NotFoundException(ITEM_NOT_FOUND));
        Optional<Favorite> favorite = favoriteRepository.findByUserIdAndItemId(userId, itemId);
        return ItemResponse.from(findItem, findItem.getItemImages(), favorite.isPresent());
    }

    public List<ItemResponse> findBySearchRequest(Long userId, ItemSearchRequest request) {
        return itemQueryRepository.findBySearchCondition(userId, request.getItemId(), request.getCategoryId(),
                request.getMajorId(), request.getSearchWord(), request.getSize());
    }

    public List<ItemResponse> findBySeller(Long sellerId) {
        return itemQueryRepository.findBySellerId(sellerId);
    }

}
