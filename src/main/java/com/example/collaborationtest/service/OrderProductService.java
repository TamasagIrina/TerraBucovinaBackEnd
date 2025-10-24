package com.example.collaborationtest.service;
import com.example.collaborationtest.model.OrderProduct;
import com.example.collaborationtest.repository.OrderRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderProductService {

    private OrderRepo orderRepo;

    public OrderProductService(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }

    public List<OrderProduct> getProducts(int orderId) {
        return this.orderRepo.findById(orderId).get().getProducts();
    }


}
