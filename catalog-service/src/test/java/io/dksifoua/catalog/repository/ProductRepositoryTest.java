package io.dksifoua.catalog.repository;

import io.dksifoua.catalog.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private IProductRepository underTest;

    private Product product01;

    @BeforeEach
    public void setUp() {
        this.product01 = Product
                .builder()
                .name("Product01")
                .description("Description of product 01")
                .price(BigDecimal.valueOf(10L))
                .build();
    }

    @Test
    @DisplayName(value = "Get a product by its name when the product exists unit test")
    public void givenAProductName_whenFindByName_thenReturnTheProductIfTheProductExists() {
        Product savedProduct = this.underTest.save(this.product01);

        Optional<Product> product = this.underTest.findByName(this.product01.getName());
        assertThat(product).isNotEmpty();
        assertThat(product.get().getId()).isEqualTo(savedProduct.getId());
        assertThat(product.get().getName()).isEqualTo(this.product01.getName());
        assertThat(product.get().getDescription()).isEqualTo(this.product01.getDescription());
    }

    @Test
    @DisplayName(value = "Get a product by its name when when the product doesn't exist unit test")
    public void givenAProductName_whenFindByName_thenReturnEmptyIfTheProductDoesntExist() {
        Optional<Product> product = this.underTest.findByName(this.product01.getName());
        assertThat(product).isEmpty();
    }
}
