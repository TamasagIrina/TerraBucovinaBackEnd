package com.example.collaborationtest.service;
import com.example.collaborationtest.model.OrderProduct;
import com.example.collaborationtest.repository.OrderProductRepo;
import com.example.collaborationtest.repository.OrderRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderProductService {

    private OrderProductRepo orderProductRepo;

    public OrderProductService(OrderProductRepo orderProductRepo) {
      this.orderProductRepo = orderProductRepo;
    }

    public OrderProduct save(OrderProduct orderProduct) {
        return orderProductRepo.save(orderProduct);
    }






}
