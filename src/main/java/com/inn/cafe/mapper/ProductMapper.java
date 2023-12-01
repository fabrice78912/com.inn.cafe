package com.inn.cafe.mapper;

import com.inn.cafe.POJO.Product;
import com.inn.cafe.wrapper.ProductWrapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Optional;


@Mapper(componentModel = "spring")
public interface ProductMapper {


    @Mapping(target = "categoryId", source = "category.id ")
    @Mapping(target = "categoryName", source = "category.name ")
    ProductWrapper entityToDto(Product entity);

    List<ProductWrapper> listEntityToListDto(List<Product> entities);


    Product dtoToEntity(ProductWrapper dto);

    List<Product> listDtoToEntities(List<ProductWrapper> listDto);

    Product findByName(String email);
}
