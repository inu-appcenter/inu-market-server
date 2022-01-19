package inu.market.item;

import inu.market.item.domain.Item;
import inu.market.item.domain.ItemImage;
import inu.market.item.domain.Status;
import inu.market.item.dto.*;
import inu.market.major.MajorFixture;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static inu.market.CommonFixture.TEST_IMAGE_URL;
import static inu.market.CommonFixture.TEST_SIZE;
import static inu.market.category.CategoryFixture.TEST_CATEGORY;
import static inu.market.category.CategoryFixture.TEST_CATEGORY_RESPONSE;
import static inu.market.major.MajorFixture.TEST_MAJOR;
import static inu.market.user.UserFixture.TEST_USER;
import static inu.market.user.UserFixture.TEST_USER_RESPONSE;

public class ItemFixture {

    public static final String TEST_ITEM_TITLE = "제목";
    public static final String TEST_ITEM_CONTENTS = "설명";
    public static final int TEST_ITEM_FAVORITE_COUNT = 0;
    public static final int TEST_ITEM_PRICE = 1000;
    public static final Status TEST_ITEM_STATUS = Status.SALE;
    public static final Boolean TEST_ITEM_FAVORITE = true;

    public static final Item TEST_ITEM
            = new Item(1L, TEST_ITEM_TITLE, TEST_ITEM_CONTENTS, TEST_IMAGE_URL, TEST_ITEM_PRICE, TEST_ITEM_FAVORITE_COUNT, TEST_ITEM_STATUS, TEST_MAJOR, TEST_CATEGORY, TEST_USER, new ArrayList<>());

    public static final ItemImage TEST_ITEM_IMAGE = new ItemImage(1L, TEST_IMAGE_URL, TEST_ITEM);

    public static final ItemCreateRequest TEST_ITEM_CREATE_REQUEST
            = new ItemCreateRequest(TEST_ITEM_TITLE, TEST_ITEM_CONTENTS, TEST_ITEM_PRICE, TEST_MAJOR.getId(), TEST_CATEGORY.getId(), Arrays.asList(TEST_IMAGE_URL));

    public static final ItemUpdateRequest TEST_ITEM_UPDATE_REQUEST
            = new ItemUpdateRequest(TEST_ITEM_TITLE, TEST_ITEM_CONTENTS, TEST_ITEM_PRICE, TEST_MAJOR.getId(), TEST_CATEGORY.getId(), Arrays.asList(TEST_IMAGE_URL));

    public static final ItemUpdateStatusRequest TEST_ITEM_UPDATE_STATUS_REQUEST
            = new ItemUpdateStatusRequest(TEST_ITEM_STATUS.getStatus());

    public static final ItemSearchRequest TEST_ITEM_SEARCH_REQUEST
            = new ItemSearchRequest(TEST_CATEGORY.getId(), TEST_MAJOR.getId(), TEST_ITEM_TITLE, TEST_SIZE, TEST_ITEM.getId());

    public static final ItemResponse TEST_ITEM_SIMPLE_RESPONSE
            = new ItemResponse(TEST_ITEM.getId(), TEST_ITEM.getTitle(), null,
            TEST_ITEM.getMainImageUrl(), TEST_ITEM.getPrice(), TEST_ITEM.getFavoriteCount(),
            TEST_ITEM.getStatus().getStatus(), null, LocalDateTime.now(),
            LocalDateTime.now(), null, null, null, null);

    public static final ItemResponse TEST_ITEM_RESPONSE
            = new ItemResponse(TEST_ITEM.getId(), TEST_ITEM.getTitle(), TEST_ITEM.getContents(),
            null, TEST_ITEM.getPrice(), TEST_ITEM.getFavoriteCount(),
            TEST_ITEM.getStatus().getStatus(), TEST_ITEM_FAVORITE, LocalDateTime.now(),
            LocalDateTime.now(), MajorFixture.TEST_MAJOR_RESPONSE,
            TEST_CATEGORY_RESPONSE, TEST_USER_RESPONSE, Arrays.asList(TEST_IMAGE_URL));
}


