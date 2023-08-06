package com.mudit.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mudit.common.entity.order.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
