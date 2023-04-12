package io.dksifoua.catalog.service;

import io.dksifoua.catalog.entity.Category;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {

    Category saveCategory(Category category);
    List<Category> getAllCategories();
    Optional<Category> getCategoryById(long id);
    void deleteCategoryById(long id);
}
