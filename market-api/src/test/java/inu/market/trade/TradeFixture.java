package inu.market.trade;

import inu.market.item.ItemFixture;
import inu.market.trade.dto.TradeCreateRequest;
import inu.market.user.UserFixture;

import static inu.market.item.ItemFixture.*;
import static inu.market.user.UserFixture.*;

public class TradeFixture {

    public static final TradeCreateRequest TEST_TRADE_CREATE_REQUEST
            = new TradeCreateRequest(TEST_USER.getId(), TEST_ITEM.getId());
}
