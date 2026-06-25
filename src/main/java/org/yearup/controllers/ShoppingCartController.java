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

@RestController
@RequestMapping("cart")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class ShoppingCartController
{
    // a shopping cart controller depends on the service layer
    private final ShoppingCartService shoppingCartService;
    private final UserService userService;

@Autowired
public ShoppingCartController(ShoppingCartService shoppingCartService,UserService userService){
    this.shoppingCartService= shoppingCartService;
    this.userService= userService;
}


    // each method in this controller requires a Principal object as a parameter
    public ShoppingCart getCart(Principal principal)
    {
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();
        return shoppingCartService.getByUserId(userId);
    }


    @PostMapping("products/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingCart addToCart(@PathVariable int productId, Principal principal){

        User user = userService.getByUserName(principal.getName());
        return shoppingCartService.addProduct(user.getId(), productId);


    }

    @PutMapping("products/{productId}")
    public ShoppingCart updateCart(@PathVariable int productId,
                                   @RequestBody ShoppingCartItem item,
                                   Principal principal)
    {
        User user = userService.getByUserName(principal.getName());
        return shoppingCartService.updateProduct(user.getId(), productId, item.getQuantity());
    }




    @DeleteMapping
    public ShoppingCart clearCart(Principal principal)
    {
        User user = userService.getByUserName(principal.getName());
        return shoppingCartService.clearCart(user.getId());
    }



}
