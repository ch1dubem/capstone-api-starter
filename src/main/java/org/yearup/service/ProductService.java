package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.Product;
import org.yearup.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService
{
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository)
    {
        this.productRepository = productRepository;
    }

    // Searches products with optional filters (category, price range, subcategory).
    // BUG 1 FIX: the old code ended this stream with .filter(Product::isFeatured),
    // which hid every non-featured product. Removing it lets search return the full
    // list of products that match the filters that were actually provided.
    public List<Product> search(Integer categoryId, Double minPrice, Double maxPrice, String subCategory)
    {
        // start from one category's products if a category was given, otherwise every product
        List<Product> products = categoryId != null
                ? productRepository.findByCategoryId(categoryId)
                : productRepository.findAll();

        // each filter is skipped when its parameter is null ("no filter" = keep everything)
        return products.stream()
                .filter(p -> minPrice == null || p.getPrice() >= minPrice)
                .filter(p -> maxPrice == null || p.getPrice() <= maxPrice)
                .filter(p -> subCategory == null || subCategory.equalsIgnoreCase(p.getSubCategory()))
                .toList();
    }

    public List<Product> listByCategoryId(int categoryId)
    {
        return productRepository.findByCategoryId(categoryId);
    }

    public Product getById(int productId)
    {
        return productRepository.findById(productId).orElse(null);
    }

    public Product create(Product product)
    {
        product.setProductId(0);
        return productRepository.save(product);
    }

    // Updates an existing product by loading it, copying the new values onto it, then saving.
    public Product update(int productId, Product product)
    {
        // load the current row so we update the right record (throws if the id doesn't exist)
        Product existing = productRepository.findById(productId).orElseThrow();
        existing.setName(product.getName());
        existing.setPrice(product.getPrice());
        existing.setCategoryId(product.getCategoryId());
        existing.setDescription(product.getDescription());
        existing.setSubCategory(product.getSubCategory());
        existing.setStock(product.getStock());      // BUG 2 FIX: this line was missing, so stock edits never saved
        existing.setFeatured(product.isFeatured());
        existing.setImageUrl(product.getImageUrl());
        return productRepository.save(existing);     // becomes an UPDATE because the id already exists
    }

    public void delete(int productId)
    {
        productRepository.deleteById(productId);
    }
}
