package com.postrowski.ecommerceapp.repository;

import com.postrowski.ecommerceapp.model.Order;
import com.postrowski.ecommerceapp.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {

    @Query(value = "from Order o where o.created BETWEEN :startDate AND :endDate")
    List<Order> findAllByCreatedBetween(@Param("startDate") Instant startDate, @Param("endDate")  Instant endDate);
}
