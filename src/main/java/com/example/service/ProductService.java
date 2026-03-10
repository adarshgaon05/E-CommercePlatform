package com.example.service;

import com.example.entity.Product;
import com.example.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Add Product
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    // Get All Products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Get Product By Id
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    // Delete Product
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
 // Update Product
    public Product updateProduct(Long id, Product updatedProduct) {
        return productRepository.findById(id).map(product -> {
            product.setName(updatedProduct.getName());
            product.setDescription(updatedProduct.getDescription());
            product.setPrice(updatedProduct.getPrice());
            product.setQuantity(updatedProduct.getQuantity());
            return productRepository.save(product);
        }).orElse(null);
    }
}