package com.inn.cafe.serviceImpl;

import com.inn.cafe.JWT.JwtFilter;
import com.inn.cafe.POJO.Category;
import com.inn.cafe.POJO.Product;
import com.inn.cafe.POJO.User;
import com.inn.cafe.wrapper.ProductWrapper;
import com.inn.cafe.constents.CafeConstants;
import com.inn.cafe.dao.ProductDao;
import com.inn.cafe.mapper.ProductMapper;
import com.inn.cafe.service.ProductService;
import com.inn.cafe.utils.CafeUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@Slf4j
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {


    private final ProductDao productDao;


    private final JwtFilter jwtFilter;

    private final ProductMapper productMapper;


    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
        log.info("Inside addNewCategory {}", requestMap);

        try {
            if (jwtFilter.isAdmin()) { // check if current user is Admin
                if (validateProductyMap(requestMap, false)) {
                    productDao.save(getProductFromMap(requestMap, false));
                    return CafeUtils.getResponseEntity("Product added successfully!!", HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }


    private Product getProductFromMap(Map<String, String> requestMap, boolean isAdd) {
        Category category = new Category();
        category.setId(Integer.parseInt(requestMap.get("categoryId")));

        Product product = new Product();
        if (isAdd) {
            product.setId(Integer.parseInt(requestMap.get("id")));
        } else {
            product.setStatus("true");
        }
        product.setCategory(category);
        product.setName(requestMap.get("name"));
        product.setDescription(requestMap.get("description"));
        product.setPrice(Integer.parseInt(requestMap.get("price")));
        return product;

    }

    private boolean validateProductyMap(Map<String, String> requestMap, boolean validatedId) {
        if (requestMap.containsKey("name")) {
            if (requestMap.containsKey("id") && validatedId) {
                return true;
            } else if (!validatedId) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
        try {
            return new ResponseEntity<>(productMapper.listEntityToListDto(productDao.findAll()), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                if (validateProductyMap(requestMap, true)) {
                    Optional<Product> optionalProduct = productDao.findById(Integer.parseInt(requestMap.get("id")));
                    if (!optionalProduct.isEmpty()) {
                        Product product = getProductFromMap(requestMap, true);
                        product.setStatus(optionalProduct.get().getStatus());
                        productDao.save(product);
                        return CafeUtils.getResponseEntity("Product updated successfully. ", HttpStatus.OK);
                    } else {
                        return CafeUtils.getResponseEntity("product id doesn't exist", HttpStatus.OK);
                    }

                } else {
                    CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
                }

            } else {
                CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {
        try {

            if (jwtFilter.isAdmin()) {
                Optional<Product> product = productDao.findById(id);
                if (!product.isEmpty()) {
                    productDao.deleteById(id);
                    return CafeUtils.getResponseEntity("Product delete successfully", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity("Id doesn't exist,", HttpStatus.NOT_FOUND);
                }

            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try {

            if (jwtFilter.isAdmin()) {
                Optional<Product> optionalProduct = productDao.findById(Integer.parseInt(requestMap.get("id")));
                if (!optionalProduct.isEmpty()) {
                    productDao.updateProductStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                    return CafeUtils.getResponseEntity("Product status updated successfully. ", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity("product id doesn't exist", HttpStatus.OK);
                }

            } else {
                CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getProductByCategory(Integer id) {
        try {
            return new ResponseEntity<>(productDao.getProductByCategory(id), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
