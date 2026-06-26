package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;
import org.yearup.service.ShoppingCartService;
import org.yearup.service.UserService;

import java.security.Principal;

// REST controller for /cart. @PreAuthorize("isAuthenticated()") secures every endpoint -
// a cart belongs to a logged-in user, so there is no public cart.
@RestController
@RequestMapping("cart")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class ShoppingCartController
{
    // a shopping cart controller depends on the service layer
    private final ShoppingCartService shoppingCartService;
    private final UserService userService;   // used to turn the logged-in username into a user id

@Autowired
public ShoppingCartController(ShoppingCartService shoppingCartService,UserService userService){
    this.shoppingCartService= shoppingCartService;
    this.userService= userService;
}


    // GET /cart - returns the logged-in user's cart (Principal -> username -> user id -> cart)
    @GetMapping
    public ShoppingCart getCart(Principal principal)
    {
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();
        return shoppingCartService.getByUserId(userId);
    }


    // POST /cart/products/{productId} - add a product to the cart, returns 201 Created
    @PostMapping("products/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingCart addToCart(@PathVariable int productId, Principal principal){

        User user = userService.getByUserName(principal.getName());
        return shoppingCartService.addProduct(user.getId(), productId);


    }

    // PUT /cart/products/{productId} - set a product's quantity (read from the request body)
    @PutMapping("products/{productId}")
    public ShoppingCart updateCart(@PathVariable int productId,
                                   @RequestBody ShoppingCartItem item,
                                   Principal principal)
    {
        User user = userService.getByUserName(principal.getName());
        return shoppingCartService.updateProduct(user.getId(), productId, item.getQuantity());
    }




    // DELETE /cart - empty the cart and return the now-empty cart (200 OK)
    @DeleteMapping
    public ShoppingCart clearCart(Principal principal)
    {
        User user = userService.getByUserName(principal.getName());
        return shoppingCartService.clearCart(user.getId());
    }



}
