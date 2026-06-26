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

    // Builds the full shopping cart for a user.
    // The database only stores bare rows (userId, productId, quantity), so we stream those rows,
    // look up the full product for each one, and add the finished items to the cart.
    public ShoppingCart getByUserId(int userId) {
        ShoppingCart cart = new ShoppingCart();

        shoppingCartRepository.findByUserId(userId)
                .stream()
                .map(row -> {                                   // turn each db row into a rich ShoppingCartItem
                    ShoppingCartItem item = new ShoppingCartItem();
                    item.setProduct(productService.getById(row.getProductId()));
                    item.setQuantity(row.getQuantity());
                    return item;
                })
                .forEach(cart::add);                            // add each item to the cart (keyed by product id)

        return cart;

    }


    // Adds a product to the cart. If it isn't there yet, insert a new row with quantity 1;
    // if it's already there, just increase the quantity (so we never get duplicate rows).
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

        return getByUserId(userId);   // return the rebuilt cart so the caller sees the latest state
    }


    // Sets the quantity of a product already in the cart to an exact value.
    // If the product isn't in the cart, do nothing (safe no-op).
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



    // Removes everything from the user's cart. @Transactional is required for a derived delete query.
    @Transactional
    public ShoppingCart clearCart(int userId)
    {
        shoppingCartRepository.deleteByUserId(userId);
        return getByUserId(userId);   // return the now-empty cart
    }

}
