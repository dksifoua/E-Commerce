package io.dksifoua.catalog.repository;

import io.dksifoua.catalog.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c where c.name = :name")
    Optional<Category> findByName(String name);
}
