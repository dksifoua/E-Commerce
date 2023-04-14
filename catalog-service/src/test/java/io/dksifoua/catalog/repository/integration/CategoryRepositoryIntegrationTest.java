package io.dksifoua.catalog.repository.integration;

import io.dksifoua.catalog.AbstractContainerBaseTest;
import io.dksifoua.catalog.entity.Category;
import io.dksifoua.catalog.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryIntegrationTest extends AbstractContainerBaseTest {

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category01;
    private Category category02;

    @BeforeEach
    public void setup() {
        category01 = Category.builder().name("Category01").description("Description01").build();
        category02 = Category.builder().name("Category02").description("Description02").build();
    }

    @Test
    @DisplayName(value = "Database container is running")
    public void databaseContainerIsRunningTest() {
        assertThat(MY_SQL_CONTAINER.isRunning()).isTrue();
    }

    @Test
    @DisplayName(value = "Get all categories unit test")
    public  void getAllCategoryTest() {
        categoryRepository.save(this.category01);
        categoryRepository.save(this.category02);

        List<Category> categories = categoryRepository.findAll();

        assertThat(categories).isNotNull();
        assertThat(categories).isNotEmpty();
        assertThat(categories.size()).isEqualTo(2);
    }

    @Test
    @DisplayName(value = "Get a category (name) unit test")
    public void getCategoryByNameTest() {
        Category savedCategory = categoryRepository.save(this.category01);

        Optional<Category> category = categoryRepository.findByName(savedCategory.getName());

        assertThat(category).isNotEmpty();
        assertThat(category.get().getName()).isEqualTo(this.category01.getName());
        assertThat(category.get().getDescription()).isEqualTo(this.category01.getDescription());
    }

    @Test
    @DisplayName(value = "Save a category unit test")
    public void saveCategoryTest() {
        Category savedCategory = categoryRepository.save(this.category01);

        assertThat(savedCategory).isNotNull();
        assertThat(savedCategory.getId()).isGreaterThan(0L);
    }

    @Test
    @DisplayName(value = "Delete a category (id) unit test")
    public void deleteCategoryTest() {
        Category savedCategory = categoryRepository.save(this.category01);

        categoryRepository.deleteById(savedCategory.getId());

        Optional<Category> category = categoryRepository.findById(savedCategory.getId());

        assertThat(category).isEmpty();
    }

    @Test
    @DisplayName(value = "Update a category (id) unit test")
    public void updateCategoryTest() {
        Category savedCategory = categoryRepository.save(this.category01);

        savedCategory.setName("Category01Updated");
        Category updatedCategory = categoryRepository.save(savedCategory);

        Optional<Category> category = categoryRepository.findById(updatedCategory.getId());

        assertThat(category).isNotEmpty();
        assertThat(category.get().getId()).isEqualTo(savedCategory.getId());
        assertThat(category.get().getName()).isEqualTo(savedCategory.getName());
        assertThat(category.get().getDescription()).isEqualTo(savedCategory.getDescription());
    }
}
