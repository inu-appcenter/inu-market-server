package inu.market.item.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static inu.market.category.domain.QCategory.category;
import static inu.market.item.domain.QItem.item;
import static inu.market.item.domain.QItemImage.itemImage;
import static inu.market.major.domain.QMajor.major;
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

    public Item findWithSellerAndItemImagesAndCategoryAndMajorById(Long itemId) {
        Item findItem = queryFactory
                .selectFrom(QItem.item).distinct()
                .leftJoin(QItem.item.seller, user).fetchJoin()
                .leftJoin(QItem.item.category, category).fetchJoin()
                .leftJoin(QItem.item.major, major).fetchJoin()
                .leftJoin(QItem.item.itemImages, itemImage).fetchJoin()
                .where(QItem.item.id.eq(itemId))
                .fetchOne();

        if (findItem == null) {
            throw new RuntimeException("존재하지 않는 상품입니다.");
        }

        return findItem;
    }
}
