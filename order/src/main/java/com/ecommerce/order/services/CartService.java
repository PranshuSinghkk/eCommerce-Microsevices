package com.ecommerce.order.services;


import com.ecommerce.order.dtos.CartItemRequest;
import com.ecommerce.order.models.CartItem;
import com.ecommerce.order.repositories.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class CartService {

    private final CartItemRepository cartItemRepository;

    public boolean addToCart(String userId, CartItemRequest request) {
//        // looking for Product
//        Optional<Product> productOpt = productRepository.findById(request.getProductId());
//        // checking for product
//        if(productOpt.isEmpty()) {
//            return false;
//        }
//
//        // if product found
//        Product product = productOpt.get();
//        // if product quantity is less than requested quantity
//        if(product.getStockQuantity() < request.getQuantity()) {
//            return false;
//        }
//
//        // looking for user
//        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
//        // if user doesn't exists
//        if(userOpt.isEmpty()) {
//            return false;
//        }
//        // if user found
//        User user = userOpt.get();
//
        // if cartItem already exists in the DB fetch it
        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId, request.getProductId());     // user and product objects id is used to fetch
        // if not null update cart item quantity
        if(existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice(BigDecimal.valueOf(1000.00));
            cartItemRepository.save(existingCartItem);
        } else {    // add to that new cart item to cart
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(BigDecimal.valueOf(1000.00));
            cartItemRepository.save(cartItem);
        }
        return true;
    }

    public boolean deleteItemFromCart(String userId, String productId) {
        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId);
        if(cartItem != null) {
            cartItemRepository.delete(cartItem);
            return true;
        }
        return false;
    }

    public List<CartItem> getCart(String userId) {
        return cartItemRepository.findByUserId(userId);
    }

    public void clearCart(String userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}

