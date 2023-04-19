package io.dksifoua.catalog.service.impl;

import io.dksifoua.catalog.entity.Category;
import io.dksifoua.catalog.exception.UniqueConstraintException;
import io.dksifoua.catalog.repository.ICategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private ICategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category01;
    private Category category02;

    @BeforeEach
    void setUp() {
        this.category01 = Category.builder().id(1L).name("Category01").description("Description01").build();
        this.category02 = Category.builder().id(2L).name("Category02").description("Description02").build();
    }

    @Test
    @DisplayName(value = "Save a category")
    public void saveCategoryTest() {
        given(this.categoryRepository.findByName(this.category01.getName())).willReturn(Optional.empty());
        given(this.categoryRepository.save(this.category01))
                .willReturn(
                    Category
                            .builder()
                            .id(this.category01.getId())
                            .name(this.category01.getName())
                            .description(this.category01.getDescription())
                            .build()
                );

        Category savedCategory01 = this.categoryService.saveCategory(this.category01);
        assertThat(savedCategory01.getId()).isEqualTo(this.category01.getId());
    }

    @Test
    @DisplayName(value = "Save a category that exists")
    public void saveCategoryWithUniqueConstraintExceptionTest() {
        given(this.categoryRepository.findByName(this.category01.getName())).willReturn(Optional.of(this.category01));
        assertThrows(UniqueConstraintException.class, () -> this.categoryService.saveCategory(this.category01));
        verify(this.categoryRepository, never()).save(any(Category.class));
    }

    @Test
    @DisplayName(value = "Get all categories")
    public void getAllCategoriesTest() {
        given(this.categoryRepository.findAll()).willReturn(List.of(this.category01, this.category02));
        List<Category> categories = this.categoryService.getAllCategories();
        assertThat(categories).isNotNull();
        assertThat(categories).isNotEmpty();
        assertThat(categories.size()).isEqualTo(2);
    }

    @Test
    @DisplayName(value = "Get all categories with empty table")
    public void getAllCategoriesWithEmptyTableTest() {
        given(this.categoryRepository.findAll()).willReturn(Collections.emptyList());
        List<Category> categories = this.categoryService.getAllCategories();
        assertThat(categories).isNotNull();
        assertThat(categories).isEmpty();
    }

    @Test
    @DisplayName(value = "Get a category by ID")
    public void getCategoryByIdTest() {
        given(this.categoryRepository.findById(this.category02.getId())).willReturn(Optional.of(this.category02));
        Optional<Category> category = this.categoryService.getCategoryById(this.category02.getId());
        assertThat(category).isNotEmpty();
        assertThat(category.get().getId()).isEqualTo(this.category02.getId());
        assertThat(category.get().getName()).isEqualTo(this.category02.getName());
        assertThat(category.get().getDescription()).isEqualTo(this.category02.getDescription());
    }

    @Test
    @DisplayName(value = "Get a category by ID")
    public void updateCategoryTest() {
        given(this.categoryRepository.save(this.category01)).willReturn(this.category01);
        this.category01.setName(this.category02.getName() + "Updated");
        this.category01.setDescription(this.category02.getDescription() + "Updated");
        Category updatedCategory = this.categoryService.saveCategory(this.category01);
        assertThat(updatedCategory.getId()).isEqualTo(this.category01.getId());
        assertThat(updatedCategory.getName()).isEqualTo(this.category01.getName());
        assertThat(updatedCategory.getDescription()).isEqualTo(this.category01.getDescription());

    }

    @Test
    @DisplayName(value = "Delete a category by ID")
    public void deleteCategoryById() {
        willDoNothing().given(this.categoryRepository).deleteById(this.category01.getId());
        this.categoryService.deleteCategoryById(this.category01.getId());
        verify(this.categoryRepository, times(1)).deleteById(this.category01.getId());
    }
}