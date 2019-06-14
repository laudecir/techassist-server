package com.hasckel.techassist.repository;

import com.hasckel.techassist.model.OrderService;
import com.hasckel.techassist.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderServiceRepository extends JpaRepository<OrderService, Long> {

    @Query("FROM OrderService os WHERE os.status <> 'CANCELED'")
    Page<OrderService> findAll(Pageable pageable);

    @Query("FROM OrderService os WHERE os.status <> 'CANCELED' AND os.technical IS NULL OR os.technical = :user")
    Page<OrderService> findAllTechnicalUser(@Param("user") User user, Pageable pageable);

}
