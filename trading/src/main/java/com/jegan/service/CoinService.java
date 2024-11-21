package com.jegan.service;

import com.jegan.model.Coin;

import java.util.List;

public interface CoinService {

    List<Coin> getCoinList(int page) throws Exception;


    String getMarketChart(String coinId, int days) throws Exception;
    String getCoinsDEtails(String coinId) throws Exception;
    Coin findById(String coinId) throws Exception;
    String searchCoin(String keyword) throws Exception;

    String getTop50COinsByMarketCapRank() throws Exception;

    String getTradingCoins() throws Exception;
}
