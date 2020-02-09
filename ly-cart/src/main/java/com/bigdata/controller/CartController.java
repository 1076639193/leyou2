package com.bigdata.controller;

import com.bigdata.pojo.Cart;
import com.bigdata.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart){
        cartService.addCart(cart);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping
    public ResponseEntity<List<Cart>> queryCarts(){
        List<Cart> carts=cartService.queryCarts();
        return ResponseEntity.ok(carts);

    }
    @PutMapping("increment")
    public ResponseEntity<Void> increment(@RequestBody Cart cart){
        cartService.increment(cart);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable("id") String id){
        cartService.delete(id);
        return ResponseEntity.ok().build();
    }
}
