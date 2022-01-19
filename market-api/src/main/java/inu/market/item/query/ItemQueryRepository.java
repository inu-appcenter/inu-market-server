package inu.market.item.query;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import inu.market.item.domain.Item;
import inu.market.item.domain.Status;
import inu.market.item.dto.ItemResponse;
import inu.market.item.dto.QItemResponse;
import inu.market.trade.domain.QTrade;
import inu.market.trade.domain.Trade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static inu.market.block.domain.QBlock.block;
import static inu.market.item.domain.QItem.item;
import static inu.market.trade.domain.QTrade.*;


@Repository
@RequiredArgsConstructor
public class ItemQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<ItemResponse> findByTradeBuyerId(Long buyerId) {
        return queryFactory
                .select(new QItemResponse(item.id, item.title, item.mainImageUrl, item.price, item.favoriteCount,
                        item.status.stringValue(), item.createdAt, item.updatedAt))
                .from(trade)
                .join(trade.item, item)
                .where(trade.buyer.id.eq(buyerId))
                .orderBy(trade.id.desc())
                .fetch();
    }

    public List<ItemResponse> findBySellerId(Long sellerId) {
        return queryFactory
                .select(new QItemResponse(item.id, item.title, item.mainImageUrl, item.price, item.favoriteCount,
                        item.status.stringValue(), item.createdAt, item.updatedAt))
                .from(item)
                .where(item.seller.id.eq(sellerId))
                .orderBy(item.id.desc())
                .fetch();
    }

    public List<ItemResponse> findBySearchCondition(Long userId, Long itemId, Long categoryId, Long majorId,
                                                    String searchWord, Integer size) {
        return queryFactory
                .select(new QItemResponse(item.id, item.title, item.mainImageUrl, item.price, item.favoriteCount,
                        item.status.stringValue(), item.createdAt, item.updatedAt))
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
