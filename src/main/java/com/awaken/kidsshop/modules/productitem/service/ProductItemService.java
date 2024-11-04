package com.awaken.kidsshop.modules.productitem.service;

import com.awaken.kidsshop.dto.ProductItemDTO;
import com.awaken.kidsshop.modules.product.entity.Product;
import com.awaken.kidsshop.modules.productitem.entity.ProductItem;
import com.awaken.kidsshop.modules.size.entity.Size;
import com.awaken.kidsshop.modules.productitem.repository.ProductItemRepository;
import com.awaken.kidsshop.modules.product.repository.ProductRepository;
import com.awaken.kidsshop.modules.size.repository.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductItemService {
    private final ProductItemRepository productItemRepository;
    private final ProductRepository productRepository;
    private final SizeRepository sizeRepository;

    @Autowired
    public ProductItemService(ProductItemRepository productItemRepository, SizeRepository sizeRepository, ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.sizeRepository = sizeRepository;
        this.productItemRepository = productItemRepository;
    }

    public List<ProductItem> allProductItems() {
        return productItemRepository.findAll();
    }

    public List<ProductItem> productItemsByQuantity() {
        return productItemRepository.findAllByOrderByQuantityDesc();
    }

    public ProductItem createProductItem(ProductItemDTO productItemDTO){
        ProductItem productItem = new ProductItem();
        return getProductItem(productItemDTO, productItem);
    }

    private ProductItem getProductItem(ProductItemDTO productItemDTO, ProductItem productItem) {
        productItem.setName(productItemDTO.getName());
        productItem.setQuantity(productItemDTO.getQuantity());
        Size size = sizeRepository.findById(productItemDTO.getSizeId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Product product = productRepository.findById(productItemDTO.getProductId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        productItem.setSize(size);
        productItem.setProduct(product);
        return productItemRepository.save(productItem);
    }

    public boolean deleteProductItem(Long productItemId) {
        if (productItemRepository.findById(productItemId).isPresent()) {
            productItemRepository.deleteById(productItemId);
            return true;
        }
        return false;
    }

    public ProductItem updateProductItem(Long id, ProductItemDTO productItemDTO){
        ProductItem productItem = productItemRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return getProductItem(productItemDTO, productItem);
    }
}
