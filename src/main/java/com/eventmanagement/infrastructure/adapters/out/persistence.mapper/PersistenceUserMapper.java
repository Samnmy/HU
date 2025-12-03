package com.eventmanagement.infrastructure.adapters.out.persistence.mapper;

import com.eventmanagement.domain.model.User;
import com.eventmanagement.infrastructure.adapters.out.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PersistenceUserMapper {

    PersistenceUserMapper INSTANCE = Mappers.getMapper(PersistenceUserMapper.class);

    UserEntity toEntity(User user);

    User toDomain(UserEntity entity);
}