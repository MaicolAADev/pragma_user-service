package com.pragma.auth.r2dbc.adapter;


import com.pragma.auth.model.user.User;
import com.pragma.auth.model.user.gateways.UserPersistencePort;
import com.pragma.auth.r2dbc.entity.UserEntity;
import com.pragma.auth.r2dbc.repository.UserRepository;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserPersistencePort {

    private final UserRepository repository;

    @Override
    public Mono<User> registerUser(User user) {
        UserEntity entity = toEntity(user);
        return repository.save(entity).map(this::toDomain);
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    private UserEntity toEntity(User user){
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setBirthDate(user.getBirthDate());
        userEntity.setAddress(user.getAddress());
        userEntity.setPhone(user.getPhone());
        userEntity.setEmail(user.getEmail());
        userEntity.setBaseSalary(user.getBaseSalary());
        return userEntity;
    }

    private User toDomain(UserEntity userEntity){
        return new User(
                userEntity.getId(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getBirthDate(),
                userEntity.getAddress(),
                userEntity.getPhone(),
                userEntity.getEmail(),
                userEntity.getBaseSalary()
        );
    }
}

