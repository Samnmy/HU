package com.eventmanagement.infrastructure.adapters.out.persistence.adapter;

import com.eventmanagement.domain.model.User;
import com.eventmanagement.domain.ports.out.UserRepositoryPort;
import com.eventmanagement.infrastructure.adapters.out.persistence.entity.UserEntity;
import com.eventmanagement.infrastructure.adapters.out.persistence.mapper.PersistenceUserMapper;
import com.eventmanagement.infrastructure.adapters.out.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserJpaAdapter implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;
    private final PersistenceUserMapper persistenceUserMapper;

    @Override
    public User save(User user) {
        UserEntity userEntity = persistenceUserMapper.toEntity(user);
        UserEntity savedEntity = userJpaRepository.save(userEntity);
        return persistenceUserMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findById(id)
                .map(persistenceUserMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username)
                .map(persistenceUserMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(persistenceUserMapper::toDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userJpaRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }
}