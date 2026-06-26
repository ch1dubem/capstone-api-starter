package org.yearup.repository;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yearup.models.Product;
import org.yearup.service.ProductService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Unit tests for ProductService using Mockito (no real database).
// @Mock fakes the repository; @InjectMocks builds the real service with that fake inside.
@ExtendWith(MockitoExtension.class)
class ProductServiceTest
{
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    // BUG 1: with no filters, search must return BOTH products (featured and non-featured)
    @Test
    void search_NoFilters_ReturnsAllIncludingNonFeatured()
    {
        // Arrange
        Product featured    = new Product(1, "Laptop", 800, 1, "d", "Computers", 5, true,  "img");
        Product notFeatured = new Product(2, "Mouse",  20,  1, "d", "Computers", 5, false, "img");
        when(productRepository.findAll()).thenReturn(List.of(featured, notFeatured));

        // Act
        List<Product> found = productService.search(null, null, null, null);

        // Assert
        assertEquals(2, found.size());
    }

    // the price filter still works: only products priced >= minPrice come back
    @Test
    void search_MinPriceFilter_ReturnsOnlyAboveMin()
    {
        // Arrange
        Product high = new Product(1, "Laptop", 800, 1, "d", "Computers", 5, true, "img");
        Product low  = new Product(2, "Mouse",  20,  1, "d", "Computers", 5, true, "img");
        when(productRepository.findAll()).thenReturn(List.of(high, low));

        // Act
        List<Product> found = productService.search(null, 100.0, null, null);

        // Assert
        assertEquals(1, found.size());
        assertEquals("Laptop", found.get(0).getName());
    }

    // BUG 2: updating stock must actually save the new value
    @Test
    void update_ChangedStock_PersistsNewStock()
    {
        // Arrange
        Product existing = new Product(1, "Laptop", 800, 1, "d", "Computers", 5, true, "img");
        when(productRepository.findById(1)).thenReturn(Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Product changes = new Product(1, "Laptop", 800, 1, "d", "Computers", 99, true, "img");
        Product saved = productService.update(1, changes);

        // Assert
        assertEquals(99, saved.getStock());
        verify(productRepository).save(existing);
    }
}
