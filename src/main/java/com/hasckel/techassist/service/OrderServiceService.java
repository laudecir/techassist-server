package com.hasckel.techassist.service;

import com.hasckel.techassist.exception.ResourceNotFoundException;
import com.hasckel.techassist.model.OrderService;
import com.hasckel.techassist.model.Role;
import com.hasckel.techassist.model.User;
import com.hasckel.techassist.repository.OrderServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderServiceService {

    @Autowired
    private OrderServiceRepository orderServiceRepository;

    public Page<OrderService> getPagedResults(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        if (user.hasRole(Role.RoleType.TECHNICAL)) {
            return orderServiceRepository.findAllTechnicalUser(user, pageable);
        }

        return orderServiceRepository.findAll(pageable);
    }

    public OrderService save(OrderService OrderService) {
        return orderServiceRepository.save(OrderService);
    }

    public void delete(Long id) {
        OrderService orderService = findById(id);
        orderServiceRepository.delete(orderService);
    }

    public OrderService findById(Long id) {
        return orderServiceRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order Service", "id", id));
    }

    public void start(User user, Long id) {
        OrderService orderService = findById(id);
        orderService.setTechnical(user);
        orderService.setStatus(OrderService.Status.IN_PROGRESS);
        save(orderService);
    }

}
