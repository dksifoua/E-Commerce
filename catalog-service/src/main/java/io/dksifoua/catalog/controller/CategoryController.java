package io.dksifoua.catalog.controller;

import io.dksifoua.catalog.entity.Category;
import io.dksifoua.catalog.service.ICategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/categories")
public class CategoryController {

    private final ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category createCategory(@RequestBody Category category) {
        return this.categoryService.saveCategory(category);
    }

    @GetMapping
    public List<Category> getAllCategories() {
        return this.categoryService.getAllCategories();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable long id) {
        return this.categoryService
                .getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Category> updateCategoryById(@PathVariable long id, @RequestBody Category category) {
        return this.categoryService
                .getCategoryById(id)
                .map(savedCategory -> {
                    savedCategory.setName(category.getName());
                    savedCategory.setDescription(category.getDescription());

                    return this.categoryService.saveCategory(savedCategory);
                })
                .map(updatedCategory -> new ResponseEntity<>(updatedCategory, HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteCategoryById(@PathVariable long id) {
        this.categoryService.deleteCategoryById(id);

        return new ResponseEntity<>(
                String.format("The category [%s] has been deleted successfully.", id),
                HttpStatus.OK
        );
    }
}
