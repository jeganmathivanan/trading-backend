package com.jegan.service;

import com.jegan.Domain.OrderType;
import com.jegan.model.Coin;
import com.jegan.model.Order;
import com.jegan.model.OrderItem;
import com.jegan.model.User;

import java.util.List;

public interface OrderService  {

   Order createOrder(User user, OrderItem orderItem, OrderType orderType);

   Order getOrderById(Long orderId) throws Exception;

   List<Order> getAllOrderOfUser(Long userId,String OrderType,String assetSymbol);

   Order processOrder(Coin coin, double quantity, OrderType orderType, User user);


}

