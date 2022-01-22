package inu.market.item.query;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import inu.market.item.domain.Status;
import inu.market.item.dto.ItemResponse;
import inu.market.item.dto.QItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static inu.market.block.domain.QBlock.block;
import static inu.market.favorite.domain.QFavorite.favorite;
import static inu.market.item.domain.QItem.item;
import static inu.market.trade.domain.QTrade.trade;


@Repository
@RequiredArgsConstructor
public class ItemQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<ItemResponse> findByFavoriteUserId(Long userId) {
        return queryFactory
                .select(new QItemResponse(item))
                .from(favorite)
                .join(favorite.item, item)
                .where(favorite.user.id.eq(userId))
                .orderBy(favorite.id.desc())
                .fetch();
    }

    public List<ItemResponse> findByTradeBuyerId(Long buyerId) {
        return queryFactory
                .select(new QItemResponse(item))
                .from(trade)
                .join(trade.item, item)
                .where(trade.buyer.id.eq(buyerId))
                .orderBy(trade.id.desc())
                .fetch();
    }

    public List<ItemResponse> findBySellerId(Long sellerId) {
        return queryFactory
                .select(new QItemResponse(item))
                .from(item)
                .where(item.seller.id.eq(sellerId))
                .orderBy(item.id.desc())
                .fetch();
    }

    public List<ItemResponse> findBySearchCondition(Long userId, Long itemId, Long categoryId, Long majorId,
                                                    String searchWord, Integer size) {
        return queryFactory
                .select(new QItemResponse(item))
                .from(item)
                .where(titleLike(searchWord),
                        categoryEq(categoryId),
                        majorEq(majorId),
                        itemIdLt(itemId),
                        item.status.eq(Status.SALE),
                        item.seller.id.notIn(
                                JPAExpressions
                                        .select(block.target.id)
                                        .from(block)
                                        .where(block.user.id.eq(userId))
                        )
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
