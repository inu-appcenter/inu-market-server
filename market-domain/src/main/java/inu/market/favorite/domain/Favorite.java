package inu.market.favorite.domain;

import inu.market.item.domain.Item;
import inu.market.user.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static Favorite createFavorite(User findUser, Item findItem) {
        Favorite favorite = new Favorite();
        favorite.item = findItem;
        favorite.user = findUser;
        findItem.increaseFavoriteCount();
        return favorite;
    }

}
