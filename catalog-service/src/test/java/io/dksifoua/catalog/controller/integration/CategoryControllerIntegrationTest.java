package io.dksifoua.catalog.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dksifoua.catalog.AbstractContainerBaseTest;
import io.dksifoua.catalog.entity.Category;
import io.dksifoua.catalog.repository.ICategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CategoryControllerIntegrationTest extends AbstractContainerBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ICategoryRepository ICategoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Category category01;
    private Category category02;
    private List<Category> categories;

    @BeforeEach
    public void setUp() {
        this.ICategoryRepository.deleteAll();
        this.category01 = Category.builder().name("Category01").description("Description01").build();
        this.category02 = Category.builder().name("Category02").description("Description02").build();
        this.categories = List.of(this.category01, category02);
    }

    @Test
    @DisplayName(value = "Database container is running")
    public void databaseContainerIsRunningTest() {
        assertThat(MY_SQL_CONTAINER.isRunning()).isTrue();
    }

    @Test
    @DisplayName(value = "Create a category")
    public void createdCategoryIntegrationTest() throws Exception {
        this.mockMvc
                .perform(
                        post("/api/v1/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(this.objectMapper.writeValueAsString(this.category01))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(this.category01.getName())))
                .andExpect(jsonPath("$.description", is(this.category01.getDescription())));
    }

    @Test
    @DisplayName(value = "Get all categories")
    public void getAllCategoriesIntegrationTest() throws Exception {
        this.ICategoryRepository.saveAll(this.categories);
        this.mockMvc
                .perform(get("/api/v1/categories"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(this.categories.size())));
    }

    @Test
    @DisplayName(value = "Get a category by id")
    public void getCategoryByIdIntegrationTest() throws Exception {
        this.ICategoryRepository.save(this.category01);
        this.mockMvc
                .perform(get("/api/v1/categories/{id}", this.category01.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(this.category01.getName())))
                .andExpect(jsonPath("$.description", is(this.category01.getDescription())));
    }

    @Test
    @DisplayName(value = "Get a category by id that doesn't exist")
    public void getCategoryByIdWithResourceNotFoundExceptionIntegrationTest() throws Exception {
        this.mockMvc
                .perform(get("/api/v1/categories/{id}", this.categories.size() + 1))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName(value = "Update a category by id")
    public void updateCategoryByIdIntegrationTest() throws Exception {
        this.ICategoryRepository.save(this.category01);
        this.mockMvc
                .perform(
                        put("/api/v1/categories/{id}", this.category01.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(this.objectMapper.writeValueAsString(this.category02))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(this.category02.getName())))
                .andExpect(jsonPath("$.description", is(this.category02.getDescription())));
    }

    @Test
    @DisplayName(value = "Update a category by id that doesn't exist")
    public void updateCategoryByIdWithResourceNotFoundExceptionIntegrationTest() throws Exception {
        this.mockMvc
                .perform(
                        put("/api/v1/categories/{id}", this.category01.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(this.objectMapper.writeValueAsString(this.category02))
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName(value = "Delete a category by id")
    public void deleteCategoryByIdIntegrationTest() throws Exception {
        this.ICategoryRepository.save(this.category01);
        this.mockMvc
                .perform(delete("/api/v1/categories/{id}", this.category01.getId())                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}
