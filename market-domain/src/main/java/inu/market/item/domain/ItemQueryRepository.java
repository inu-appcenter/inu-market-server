package inu.market.item.domain;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static inu.market.category.domain.QCategory.category;
import static inu.market.item.domain.QItem.item;
import static inu.market.item.domain.QItemImage.itemImage;
import static inu.market.major.domain.QMajor.major;
import static inu.market.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
public class ItemQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Item> findBySellerId(Long sellerId) {
        return queryFactory
                .selectFrom(item)
                .where(item.seller.id.eq(sellerId), item.active.eq(true))
                .orderBy(item.id.desc())
                .fetch();
    }

    public Item findWithItemImagesById(Long itemId) {
        Item findItem = queryFactory
                .selectFrom(item).distinct()
                .leftJoin(item.itemImages, itemImage).fetchJoin()
                .where(item.id.eq(itemId), item.active.eq(true))
                .fetchOne();

        if (findItem == null) {
            throw new RuntimeException("존재하지 않는 상품입니다.");
        }

        return findItem;
    }

    public Item findWithSellerAndItemImagesAndCategoryAndMajorById(Long itemId) {
        Item findItem = queryFactory
                .selectFrom(item).distinct()
                .leftJoin(item.seller, user).fetchJoin()
                .leftJoin(item.category, category).fetchJoin()
                .leftJoin(item.major, major).fetchJoin()
                .leftJoin(item.itemImages, itemImage).fetchJoin()
                .where(item.id.eq(itemId), item.active.eq(true))
                .fetchOne();

        if (findItem == null) {
            throw new RuntimeException("존재하지 않는 상품입니다.");
        }

        return findItem;
    }

    public List<Item> findBySearchCondition(Long itemId, Long categoryId, Long majorId,
                                            String searchWord, int size) {
        return queryFactory
                .selectFrom(item)
                .where(titleLike(searchWord),
                        categoryEq(categoryId),
                        majorEq(majorId),
                        itemIdLt(itemId),
                        item.status.eq(Status.SALE),
                        item.active.eq(true)
                )
                .orderBy(item.id.desc())
                .limit(size)
                .fetch();
    }

    private BooleanExpression itemIdLt(Long itemId) {
        return itemId != null ? item.id.lt(itemId) : null;
    }

    private BooleanExpression titleLike(String searchWord) {
        return StringUtils.hasText(searchWord) ? item.title.contains(searchWord) : null;
    }

    private BooleanExpression categoryEq(Long categoryId) {
        return categoryId != null ? item.category.id.eq(categoryId) : null;
    }

    private BooleanExpression majorEq(Long majorId) {
        return majorId != null ? item.major.id.eq(majorId) : null;
    }

}
