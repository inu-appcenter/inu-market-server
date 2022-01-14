package inu.market.item.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static inu.market.item.domain.QItem.item;
import static inu.market.item.domain.QItemImage.itemImage;
import static inu.market.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
public class ItemQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Item findWithSellerAndItemImagesById(Long itemId) {
        Item findItem = queryFactory
                .selectFrom(item).distinct()
                .leftJoin(item.seller, user).fetchJoin()
                .leftJoin(item.itemImages, itemImage).fetchJoin()
                .where(item.id.eq(itemId))
                .fetchOne();

        if (findItem == null) {
            throw new RuntimeException("존재하지 않는 상품입니다.");
        }

        return findItem;
    }
}
