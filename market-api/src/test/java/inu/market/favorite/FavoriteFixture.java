package inu.market.favorite;

import inu.market.favorite.domain.Favorite;

import static inu.market.item.ItemFixture.TEST_ITEM;
import static inu.market.user.UserFixture.TEST_USER;

public class FavoriteFixture {

    public static final Favorite TEST_FAVORITE = new Favorite(1L, TEST_ITEM, TEST_USER);
}
