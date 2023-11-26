package com.inn.cafe.mapper;

import com.inn.cafe.POJO.User;
import com.inn.cafe.wrapper.UserWrapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserWrapper entityToDto(User entity);

    List<UserWrapper> listEntityToListDto(List<User> entities);


    User dtoToEntity(UserWrapper dto);

    List<User> listDtoToEntities(List<UserWrapper> listDto);
}
