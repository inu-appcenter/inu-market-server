package inu.market.item.service;

import inu.market.category.domain.Category;
import inu.market.category.domain.CategoryRepository;
import inu.market.client.AwsClient;
import inu.market.item.domain.Item;
import inu.market.item.domain.ItemRepository;
import inu.market.item.dto.ItemCreateRequest;
import inu.market.item.dto.ItemResponse;
import inu.market.major.domain.Major;
import inu.market.major.domain.MajorRepository;
import inu.market.user.domain.User;
import inu.market.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final MajorRepository majorRepository;
    private final AwsClient awsClient;

    public List<String> uploadImages(List<MultipartFile> images) {
        return images.stream()
                .map(image -> awsClient.upload(image))
                .collect(Collectors.toList());
    }

    @Transactional
    public ItemResponse create(Long userId, ItemCreateRequest request) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        Category findCategory = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 카테고리입니다."));

        Major findMajor = majorRepository.findById(request.getMajorId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 카테고리입니다."));

        Item item = Item.createItem(request.getTitle(), request.getContents(), request.getImageUrls().get(0),
                request.getPrice(), findUser);

        item.changeCategory(findCategory);
        item.changeMajor(findMajor);
        item.changeItemImages(request.getImageUrls());

        itemRepository.save(item);
        return ItemResponse.from(item, item.getItemImages());
    }
}
