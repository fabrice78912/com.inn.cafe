package com.inn.cafe.serviceimpl;

import com.inn.cafe.JWT.JwtFilter;
import com.inn.cafe.POJO.Category;
import com.inn.cafe.POJO.Product;
import com.inn.cafe.constents.CafeConstants;
import com.inn.cafe.dao.CategoryDao;
import com.inn.cafe.dao.ProductDao;
import com.inn.cafe.mapper.ProductMapper;
import com.inn.cafe.service.ProductService;
import com.inn.cafe.utils.CafeUtils;
import com.inn.cafe.wrapper.ProductWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {


    private final ProductDao productDao;

    private  final CategoryDao categoryDao;

    private final JwtFilter jwtFilter;

    private final ProductMapper productMapper;


    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
        log.info("Inside addNewCategory {}", requestMap);

        try {
            if (jwtFilter.isAdmin()) { // check if current user is Admin
                if (validateProductyMap(requestMap, false)) {
                    Product product = productDao.findByName(requestMap.get("name"));
                    if (Objects.isNull(product)) {
                        productDao.save(getProductFromMap(requestMap, false));
                        return CafeUtils.getResponseEntity("Product added successfully!!", HttpStatus.OK);
                    } else {
                        return CafeUtils.getResponseEntity("Product name already exist. ", HttpStatus.CONFLICT);
                    }
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
            return new ResponseEntity<>(productMapper.listEntityToListDto(productDao.findAll(Sort.by(Sort.Direction.ASC, "name"))), HttpStatus.OK);
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


                        Product product = productDao.findByNameAndIdNot(requestMap.get("name"), Integer.parseInt(requestMap.get("id")));
                        if (Objects.isNull(product)) {
                            productDao.save(getProductFromMap(requestMap, true));
                            return CafeUtils.getResponseEntity("Product updated successfully !!", HttpStatus.OK);
                        } else {
                            return CafeUtils.getResponseEntity("Product name already exist. ", HttpStatus.CONFLICT);
                        }
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

    @Override
    public ResponseEntity<Map<String,List<Product>>> getProductByCategory() {

        List<Product> productList = productDao.findAll();

        Map<String, List<Product>> productsByCategory = productList.stream()
                .collect(Collectors.groupingBy(product -> product.getCategory().getName()));

        return ResponseEntity.ok(productsByCategory);
    }



    @Override
    public ResponseEntity<ProductWrapper> getById(Integer id) {
        try {
            Optional<Product> product = productDao.findById(id);
            if (!product.isEmpty()) {
                return new ResponseEntity<>(productMapper.entityToDto(product.get()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ProductWrapper(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ProductWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<Long> countByCategoryId(Integer id) {
        try {
            Optional<Category> category= categoryDao.findById(id);
            if(!category.isEmpty()){
                return new ResponseEntity<>(productDao.countByCategoryId(id), HttpStatus.OK);
            }else {
                return new ResponseEntity<>(new Long(-1), HttpStatus.NOT_FOUND);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new Long(-1), HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
