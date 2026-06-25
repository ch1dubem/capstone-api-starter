package org.yearup.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.yearup.models.CartItem;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.repository.ShoppingCartRepository;

import java.util.List;

@Service
public class ShoppingCartService
{
    // a shopping cart is built from cart rows plus a product lookup for each row
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductService productService;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ProductService productService)
    {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productService = productService;
    }

    public ShoppingCart getByUserId(int userId) {
        ShoppingCart cart = new ShoppingCart();

        shoppingCartRepository.findByUserId(userId)
                .stream()
                .map(row -> {
                    ShoppingCartItem item = new ShoppingCartItem();
                    item.setProduct(productService.getById(row.getProductId()));
                    item.setQuantity(row.getQuantity());
                    return item;
                })
                .forEach(cart::add);

        return cart;

    }


    public ShoppingCart addProduct(int userId, int productId)
    {
        CartItem existing = shoppingCartRepository.findByUserIdAndProductId(userId, productId);

        if (existing == null)
        {
            CartItem row = new CartItem();
            row.setUserId(userId);
            row.setProductId(productId);
            row.setQuantity(1);
            shoppingCartRepository.save(row);
        }
        else
        {
            existing.setQuantity(existing.getQuantity() + 1);
            shoppingCartRepository.save(existing);
        }

        return getByUserId(userId);
    }


    public ShoppingCart updateProduct(int userId, int productId, int quantity)
    {
        CartItem existing = shoppingCartRepository.findByUserIdAndProductId(userId, productId);

        if (existing != null)
        {
            existing.setQuantity(quantity);
            shoppingCartRepository.save(existing);
        }

        return getByUserId(userId);
    }

    @Transactional
    public ShoppingCart clearCart(int userId)
    {
        shoppingCartRepository.deleteByUserId(userId);
        return getByUserId(userId);
    }

}
