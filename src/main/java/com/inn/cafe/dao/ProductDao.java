package com.inn.cafe.dao;

import com.inn.cafe.POJO.Product;
import com.inn.cafe.wrapper.ProductWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ProductDao extends JpaRepository<Product, Integer> {

    List<ProductWrapper> getAllProduct();

    @Modifying
    @Transactional
    @Query("update Product p set p.status =:status where p.id=:id")
    Integer updateProductStatus(@Param("status") String status, @Param("id") int id);


    List<ProductWrapper> getProductByCategory(Integer id);

    Product findByName(String name);
    Product findByNameAndIdNot(String name, Integer id);
}
