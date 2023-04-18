package io.dksifoua.catalog.repository;

import io.dksifoua.catalog.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where p.name = :name")
    Optional<Product> findByName(String name);
}
