package com.educative.ecommerce.service;


import com.educative.ecommerce.common.ApiResponse;
import com.educative.ecommerce.dto.cart.AddToCartDto;
import com.educative.ecommerce.dto.cart.CartDto;
import com.educative.ecommerce.dto.cart.CartItemDto;
import com.educative.ecommerce.exceptions.CustomException;
import com.educative.ecommerce.model.Cart;
import com.educative.ecommerce.model.Product;
import com.educative.ecommerce.model.User;
import com.educative.ecommerce.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private ProductService productService;

    @Autowired
    private CartRepository cartRepository;
    public void addToCart(AddToCartDto addToCartDto, User user) {
        //validate if the product id is valid
         Product product = productService.findById(addToCartDto.getProductId());

         Cart cart = new Cart();
         cart.setProduct(product);
         cart.setUser(user);
         cart.setQuantity(addToCartDto.getQuantity());
         cart.setCreatedDate(new Date());
        // save the cart
         cartRepository.save(cart);

    }


    public CartDto listCartItems(User user) {
        List<Cart> cartList = cartRepository.findAllByUserOrderByCreatedDateDesc(user);
        List<CartItemDto> cartItems = new ArrayList<>();
        double totalCost = 0;
        for(Cart cart: cartList){
            CartItemDto cartItemDto = new CartItemDto(cart);
            totalCost += cartItemDto.getQuantity() * cart.getProduct().getPrice();
            cartItems.add(cartItemDto);
        }

        CartDto cartDto = new CartDto();
        cartDto.setTotalCost(totalCost);
        cartDto.setCartItems(cartItems);
        return cartDto;

    }

    public void deleteCartItem(Integer cartItemId, User user) {
        //check item id first, if it belongs to user or not


        Optional<Cart> optionalCart = cartRepository.findById(cartItemId);
        if(optionalCart.isEmpty()){
            throw new CustomException("Cart item ID is invalid " + cartItemId);
        }

        Cart cart = optionalCart.get();
        if(cart.getUser() != user){
            throw new CustomException("cart item does not belong to the user " + cartItemId);
        }

        cartRepository.delete(cart);
    }
}
