package vn.hoidanit.laptopshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.hoidanit.laptopshop.model.Product;
import vn.hoidanit.laptopshop.repository.ProductRepository;
import vn.hoidanit.laptopshop.repository.RoleRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return this.productRepository.getById(id);
    }

    public Product handleSaveProduct(Product product) {
        return this.productRepository.save(product);
    }

    public Product updateProductById(Product productUpdate, boolean checkChangeImage) {
        System.out.println(">>>>>>>>>>>"+productUpdate);
        Product product = this.productRepository.getById(productUpdate.getId());

        product.setName(productUpdate.getName());
        product.setPrice(productUpdate.getPrice());
        product.setQuantity(productUpdate.getQuantity());
        product.setDetailDesc(productUpdate.getDetailDesc());
        product.setShortDesc(productUpdate.getShortDesc());
        product.setFactory(productUpdate.getFactory());
        product.setTarget(productUpdate.getTarget());

        if(checkChangeImage)
            product.setImage(productUpdate.getImage());
        
    
        return this.productRepository.save(product);
    }

    public void deleteAProduct(Long id) {
        this.productRepository.deleteById(id);
    }
}
