package com.mjurik.web.services;

import com.google.common.base.Strings;
import com.mjurik.web.crawler.db.CoinPersistence;
import com.mjurik.web.crawler.db.entity.Coin;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Marian Jurik on 20.9.2015.
 */
public enum CoinsService {
    INSTANCE;

    public List<Coin> findAll(String stringFilter) {
        List<Coin> coins = CoinPersistence.INSTANCE.listAll();
        boolean emptyFilter = Strings.isNullOrEmpty(stringFilter);

        return coins.stream().filter(coin -> {
            return emptyFilter || coin.matchFulltextSearch(stringFilter);
        }).collect(Collectors.toList());
    }
}
