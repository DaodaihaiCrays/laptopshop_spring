package vn.hoidanit.laptopshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import vn.hoidanit.laptopshop.model.User;
import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    User save(User user);
    List<User> findOneByEmail(String email);
    User getById(Long id);

    void deleteById(Long id);

    boolean existsByEmail(String email);

    User findByEmail(String email);
}
