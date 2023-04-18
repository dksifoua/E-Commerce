package io.dksifoua.catalog.repository;

import io.dksifoua.catalog.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private IProductRepository underTest;

    private Product product01;
    private Product product02;
    private List<Product> products;

    @BeforeEach
    public void setUp() {
        this.product01 = Product
                .builder()
                .name("Product01")
                .description("Description of product 01")
                .defaultPrice(BigDecimal.valueOf(10L))
                .build();
        this.product02 = Product
                .builder()
                .name("Product02")
                .description("Description of product 02")
                .defaultPrice(BigDecimal.valueOf(20L))
                .build();
        this.products = List.of(this.product01, this.product02);
    }

    @Test
    @DisplayName(value = "Save a product unit test")
    public void givenAProduct_whenSaveIt_thenReturnTheSavedProduct() {
        Product savedProduct = this.underTest.save(this.product01);

        assertThat(savedProduct.getId()).isGreaterThan(0L);
        assertThat(savedProduct.getName()).isEqualTo(this.product01.getName());
    }

    @Test
    @DisplayName(value = "Save multiple products unit test")
    public void givenAListOfProducts_whenSaveThem_thenReturnTheSavedListOfProducts() {
        List<Product> savedProducts = this.underTest.saveAll(this.products);

        assertThat(savedProducts).isNotEmpty();
        assertThat(savedProducts.size()).isEqualTo(this.products.size());
    }

    @Test
    @DisplayName(value = "Get a product by its name when the product exists unit test")
    public void givenAProductName_whenFindByName_thenReturnTheProductIfTheProductExists() {
        Product savedProduct = this.underTest.save(this.product02);

        Optional<Product> product = this.underTest.findByName(this.product02.getName());
        assertThat(product).isNotEmpty();
        assertThat(product.get().getId()).isEqualTo(savedProduct.getId());
        assertThat(product.get().getName()).isEqualTo(this.product02.getName());
        assertThat(product.get().getDescription()).isEqualTo(this.product02.getDescription());
    }

    @Test
    @DisplayName(value = "Get a product by its name when when the product doesn't exist unit test")
    public void givenAProductName_whenFindByName_thenReturnEmptyIfTheProductDoesntExist() {
        Optional<Product> product = this.underTest.findByName(this.product02.getName());
        assertThat(product).isEmpty();
    }

    @Test
    @DisplayName(value = "Update a product by its id unit test")
    public void givenAProductId_WhenUpdateIt_ThenReturnTheUpdatedProduct() {
        Product savedProduct = this.underTest.save(this.product01);

        savedProduct.setName(this.product02.getName());
        savedProduct.setDescription(this.product02.getDescription());
        this.underTest.save(savedProduct);

        Optional<Product> product = this.underTest.findById(savedProduct.getId());

        assertThat(product).isNotEmpty();
        assertThat(product.get().getName()).isEqualTo(this.product02.getName());
        assertThat(product.get().getDescription()).isEqualTo(this.product02.getDescription());
    }

    @Test
    @DisplayName(value = "Delete a product by its id unit test")
    public void givenAProductId_WhenDeleteIt_ThenItShouldNotExistAnymore() {
        List<Product> savedProducts = this.underTest.saveAll(this.products);

        this.underTest.deleteById(this.products.get(0).getId());
        Optional<Product> product = this.underTest.findById(savedProducts.get(0).getId());

        assertThat(product).isEmpty();
    }
}
