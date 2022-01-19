package inu.market.favorite;

import inu.market.favorite.domain.Favorite;
import inu.market.favorite.dto.FavoriteCreateRequest;
import inu.market.favorite.dto.FavoriteDeleteRequest;

import static inu.market.item.ItemFixture.TEST_ITEM;
import static inu.market.user.UserFixture.TEST_USER;

public class FavoriteFixture {

    public static final Favorite TEST_FAVORITE = new Favorite(1L, TEST_ITEM, TEST_USER);

    public static final FavoriteCreateRequest TEST_FAVORITE_CREATE_REQUEST = new FavoriteCreateRequest(TEST_ITEM.getId());

    public static final FavoriteDeleteRequest TEST_FAVORITE_DELETE_REQUEST = new FavoriteDeleteRequest(TEST_ITEM.getId());
}
