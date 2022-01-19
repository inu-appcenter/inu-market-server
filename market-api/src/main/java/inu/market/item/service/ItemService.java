package inu.market.item.service;

import inu.market.category.domain.Category;
import inu.market.category.domain.CategoryRepository;
import inu.market.chatroom.domain.ChatRoomRepository;
import inu.market.client.AwsClient;
import inu.market.common.NotFoundException;
import inu.market.favorite.domain.Favorite;
import inu.market.favorite.domain.FavoriteRepository;
import inu.market.item.domain.Item;
import inu.market.item.domain.ItemQueryRepository;
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
                .orElseThrow(() -> new NotFoundException(userId + "는 존재하지 않는 회원 ID 입니다."));

        Category findCategory = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundException(request.getCategoryId() + "는 존재하지 않는 카테고리 ID 입니다."));

        Major findMajor = majorRepository.findById(request.getMajorId())
                .orElseThrow(() -> new NotFoundException(request.getMajorId() + "는 존재하지 않는 학과 ID 입니다."));

        Item item = Item.createItem(request.getTitle(), request.getContents(), request.getImageUrls().get(0),
                request.getPrice(), Status.SALE, findUser);

        item.changeCategory(findCategory);
        item.changeMajor(findMajor);
        item.changeItemImages(request.getImageUrls());

        return itemRepository.save(item).getId();
    }

    @Transactional
    public void update(Long userId, Long itemId, ItemUpdateRequest request) {
        Item findItem = itemQueryRepository.findWithItemImagesById(itemId);

        if (!findItem.getSeller().getId().equals(userId)) {
            throw new AccessDeniedException("권한이 없습니다.");
        }

        Category findCategory = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundException(request.getCategoryId() + "는 존재하지 않는 카테고리 ID 입니다."));

        Major findMajor = majorRepository.findById(request.getMajorId())
                .orElseThrow(() -> new NotFoundException(request.getMajorId() + "는 존재하지 않는 학과 ID 입니다."));

        findItem.changeTitleAndContentAndPrice(request.getTitle(), request.getContents(), request.getPrice());
        findItem.changeCategory(findCategory);
        findItem.changeMajor(findMajor);
        findItem.changeItemImages(request.getImageUrls());
    }

    @Transactional
    public void updateStatus(Long userId, Long itemId, ItemUpdateStatusRequest request) {
        Item findItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(itemId + "는 존재하지 않는 상품 ID 입니다."));

        if (!findItem.getSeller().getId().equals(userId)) {
            throw new AccessDeniedException("권한이 없습니다.");
        }

        findItem.changeStatus(Status.valueOf(request.getStatus()));
    }

    @Transactional
    public void delete(Long userId, Long itemId) {
        Item findItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(itemId + "는 존재하지 않는 상품 ID 입니다."));

        if (!findItem.getSeller().getId().equals(userId)) {
            throw new AccessDeniedException("권한이 없습니다.");
        }

        tradeRepository.deleteAllByItem(findItem);
        favoriteRepository.deleteAllByItem(findItem);
        chatRoomRepository.deleteAllByItem(findItem);
        itemRepository.delete(findItem);
    }

    public ItemResponse findById(Long userId, Long itemId) {
        Item findItem = itemQueryRepository.findWithSellerAndItemImagesAndCategoryAndMajorById(itemId);
        Optional<Favorite> favorite = favoriteRepository.findByUserIdAndItemId(userId, itemId);
        return ItemResponse.from(findItem, findItem.getItemImages(), favorite.isPresent());
    }

    public List<ItemResponse> findBySearchRequest(Long userId, ItemSearchRequest request) {
        List<Item> items = itemQueryRepository.findBySearchCondition(userId, request.getItemId(), request.getCategoryId(),
                request.getMajorId(), request.getSearchWord(), request.getSize());
        return items.stream()
                .map(item -> ItemResponse.from(item))
                .collect(Collectors.toList());
    }

    public List<ItemResponse> findBySeller(Long sellerId) {
        List<Item> items = itemQueryRepository.findBySellerId(sellerId);
        return items.stream()
                .map(item -> ItemResponse.from(item))
                .collect(Collectors.toList());
    }

}
