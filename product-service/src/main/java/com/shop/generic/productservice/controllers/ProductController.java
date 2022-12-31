package com.shop.generic.productservice.controllers;

import com.shop.generic.productservice.services.ProductService;
import com.shop.generic.productservice.valueobjects.ProductVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
@Slf4j
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/all")
    public List<ProductVO> retrieveAllAvailableProducts() {
        log.info("Request made to get all products");
        return this.productService.findAllAvailableProducts();
    }

    @GetMapping("/{productId}")
    public ProductVO retrieveProductById(@PathVariable String productId){
        log.info("Request made to find product {}", productId);
        return this.productService.findProductById(Integer.parseInt(productId));
    }
}
