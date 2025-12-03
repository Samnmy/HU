package com.eventmanagement.infrastructure.adapters.out.persistence.mapper;

import com.eventmanagement.domain.model.Category;
import com.eventmanagement.infrastructure.adapters.out.persistence.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PersistenceCategoryMapper {

    PersistenceCategoryMapper INSTANCE = Mappers.getMapper(PersistenceCategoryMapper.class);

    CategoryEntity toEntity(Category category);

    Category toDomain(CategoryEntity entity);
}