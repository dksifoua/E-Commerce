package io.dksifoua.catalog.service.impl;

import io.dksifoua.catalog.entity.Category;
import io.dksifoua.catalog.exception.UniqueConstraintException;
import io.dksifoua.catalog.repository.ICategoryRepository;
import io.dksifoua.catalog.service.ICategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Dimitri Sifoua
 */
@Service
public class CategoryService implements ICategoryService {

    private final ICategoryRepository categoryRepository;

    public CategoryService(ICategoryRepository ICategoryRepository) {
        this.categoryRepository = ICategoryRepository;
    }

    @Override
    public Category saveCategory(Category category) {
        Optional<Category> savedCategory = this.categoryRepository.findByName(category.getName());
        if(savedCategory.isPresent()) {
            String savedCategoryName = savedCategory.get().getName();
            throw new UniqueConstraintException(String.format("The [%s] category already exists!", savedCategoryName));
        }

        return this.categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategories() {
        return this.categoryRepository.findAll();
    }

    @Override
    public Optional<Category> getCategoryById(long id) {
        return this.categoryRepository.findById(id);
    }

    @Override
    public void deleteCategoryById(long id) {
        this.categoryRepository.deleteById(id);
    }
}
