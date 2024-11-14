package com.awaken.kidsshop.modules.product.service;

import com.awaken.kidsshop.modules.product.controller.dto.ProductDTO;
import com.awaken.kidsshop.modules.brand.entity.Brand;
import com.awaken.kidsshop.modules.product.entity.Product;
import com.awaken.kidsshop.modules.brand.repository.BrandRepository;
import com.awaken.kidsshop.modules.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, BrandRepository brandRepository) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
    }

    public List<Product> allProducts(){
        return productRepository.findAll();
    }

    public Product createProduct(ProductDTO product){
        Product newProduct = new Product();
        return getProduct(product, newProduct);
    }


    public boolean deleteProduct(Long productId) {
        if (productRepository.findById(productId).isPresent()) {
            productRepository.deleteById(productId);
            return true;
        }
        return false;
    }

    public Product updateProduct(Long id, ProductDTO product){
        Product a = productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return getProduct(product, a);
    }

    private Product getProduct(ProductDTO product, Product a) {
        a.setName(product.getName());
        a.setDescription(product.getDescription());
        a.setPrice(product.getPrice());
        Brand brand = brandRepository.findById((product.getBrandId())).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Brand not found"));
        a.setBrand(brand);
        return productRepository.save(a);
    }
}
