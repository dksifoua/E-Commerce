package io.dksifoua.catalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dksifoua.catalog.entity.Category;
import io.dksifoua.catalog.service.ICategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ICategoryService categoryService;

    private Category category01;
    private Category category02;
    private List<Category> categories;

    @BeforeEach
    public void setUp() {
        this.category01 = Category.builder().id(1L).name("Category01").description("Description01").build();
        this.category02 = Category.builder().id(2L).name("Category02").description("Description02").build();
        this.categories = List.of(this.category01, category02);
    }

    @Test
    @DisplayName(value = "Create a category")
    public void createdCategoryTest() throws Exception {
        given(this.categoryService.saveCategory(any(Category.class))).willAnswer(
                invocation -> invocation.getArgument(0)
        );

        this.mockMvc
                .perform(
                        post("/api/v1/catalog/categories")
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
    public void getAllCategoriesTest() throws Exception {
        given(this.categoryService.getAllCategories()).willReturn(this.categories);

        this.mockMvc
                .perform(get("/api/v1/catalog/categories"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(this.categories.size())));
    }

    @Test
    @DisplayName(value = "Get a category by id")
    public void getCategoryByIdTest() throws Exception {
        given(this.categoryService.getCategoryById(any(Long.class))).willReturn(Optional.of(this.category01));
        this.mockMvc
                .perform(get("/api/v1/catalog/categories/{id}", this.category01.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(this.category01.getName())))
                .andExpect(jsonPath("$.description", is(this.category01.getDescription())));
    }

    @Test
    @DisplayName(value = "Get a category by id that doesn't exist")
    public void getCategoryByIdWithResourceNotFoundExceptionTest() throws Exception {
        given(this.categoryService.getCategoryById(any(Long.class))).willReturn(Optional.empty());

        this.mockMvc
                .perform(get("/api/v1/catalog/categories/{id}", this.categories.size() + 1))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName(value = "Update a category by id")
    public void updateCategoryByIdTest() throws Exception {
        given(this.categoryService.getCategoryById(this.category01.getId())).willReturn(Optional.of(this.category01));
        given(this.categoryService.saveCategory(any(Category.class))).willAnswer(
                invocation -> invocation.getArgument(0)
        );

        this.mockMvc
                .perform(
                        put("/api/v1/catalog/categories/{id}", this.category01.getId())
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
    public void updateCategoryByIdWithResourceNotFoundExceptionTest() throws Exception {
        given(this.categoryService.getCategoryById(any(Long.class))).willReturn(Optional.empty());

        this.mockMvc
                .perform(
                        put("/api/v1/catalog/categories/{id}", this.category01.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(this.objectMapper.writeValueAsString(this.category02))
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName(value = "Delete a category by id")
    public void deleteCategoryByIdTest() throws Exception {
        willDoNothing().given(this.categoryService).deleteCategoryById(any(Long.class));

        this.mockMvc
                .perform(delete("/api/v1/catalog/categories/{id}", this.category01.getId())                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}