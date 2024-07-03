package vn.hoidanit.laptopshop.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hoidanit.laptopshop.model.Product;
import vn.hoidanit.laptopshop.model.User;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long>{
    Product save(Product product);
    Product getById(Long id);

    void deleteById(Long id);
}
