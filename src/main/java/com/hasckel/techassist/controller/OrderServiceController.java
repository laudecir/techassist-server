package com.hasckel.techassist.controller;

import com.hasckel.techassist.annotation.CurrentUser;
import com.hasckel.techassist.model.OrderService;
import com.hasckel.techassist.model.User;
import com.hasckel.techassist.service.OrderServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/order-service")
public class OrderServiceController {

    @Autowired
    private OrderServiceService orderServiceService;

    @GetMapping
    public Page<OrderService> all(@CurrentUser User user,
                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "15") int size) {

        return orderServiceService.getPagedResults(user, page, size);
    }

    @PostMapping
    public ResponseEntity<OrderService> save(@RequestBody OrderService orderService) {
        OrderService saved = orderServiceService.save(orderService);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(saved.getId()).toUri();

        return ResponseEntity
                .created(location)
                .body(saved);
    }

    @PutMapping
    public ResponseEntity<OrderService> edit(@RequestBody OrderService orderService) {
        OrderService saved = orderServiceService.save(orderService);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        orderServiceService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderService> get(@PathVariable Long id) {
        OrderService orderService = orderServiceService.findById(id);
        return ResponseEntity.ok(orderService);
    }

    @Secured({"ROLE_TECHNICAL"})
    @PutMapping("/{id}/start")
    public ResponseEntity start(@CurrentUser User user,
                                 @PathVariable Long id) {
        orderServiceService.start(user, id);
        return ResponseEntity.ok().build();
    }

}
