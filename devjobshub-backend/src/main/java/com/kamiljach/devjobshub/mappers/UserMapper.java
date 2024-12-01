package com.kamiljach.devjobshub.mappers;

import com.kamiljach.devjobshub.dto.UserDto;
import com.kamiljach.devjobshub.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "likedOffers", ignore = true)
    @Mapping(target = "applications", ignore = true)
    @Mapping(target = "offers", ignore = true)
    UserDto userToUserDto(User user);
}
