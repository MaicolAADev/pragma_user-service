package com.crediya.r2dbc;

import com.crediya.model.user.User;
import com.crediya.r2dbc.entity.RoleEntity;
import com.crediya.r2dbc.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyReactiveRepositoryAdapterTest {

    @InjectMocks
    MyReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    MyReactiveRepository repository;

    @Mock
    RoleReactiveRepository roleRepository;

    @Mock
    ObjectMapper mapper;

    @Mock
    TransactionalOperator transactionalOperator;

    @Test
    void mustGetUserByEmail() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("test@email.com");
        userEntity.setRoleId(1L);

        RoleEntity role = new RoleEntity();
        role.setId(1L);
        role.setName("ADMIN");

        User user = new User();
        user.setId("1");
        user.setEmail("test@email.com");
        user.setRoleName("ADMIN");

        when(repository.findByEmail("test@email.com")).thenReturn(Mono.just(userEntity));
        lenient().when(roleRepository.findById(String.valueOf(1L))).thenReturn(Mono.just(role));
        when(mapper.map(userEntity, User.class)).thenReturn(user);

        Mono<User> result = repositoryAdapter.getUserByEmail("test@email.com");

        StepVerifier.create(result)
                .expectNextMatches(found -> found.getEmail().equals("test@email.com")
                        && "ADMIN".equals(found.getRoleName()))
                .verifyComplete();
    }

    @Test
    void mustSaveUser() {
        User user = new User();
        user.setEmail("test@email.com");
        user.setRoleName("ADMIN");

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("test@email.com");
        userEntity.setRoleId(1L);

        RoleEntity role = new RoleEntity();
        role.setId(1L);
        role.setName("ADMIN");

        when(mapper.map(user, UserEntity.class)).thenReturn(userEntity);
        when(repository.save(userEntity)).thenReturn(Mono.just(userEntity));
        when(transactionalOperator.transactional(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));
        lenient().when(roleRepository.findById(String.valueOf(1L))).thenReturn(Mono.just(role));

        Mono<User> result = repositoryAdapter.saveUser(user, 1L);

        StepVerifier.create(result)
                .expectNextMatches(saved -> saved.getEmail().equals("test@email.com")
                        && "ADMIN".equals(saved.getRoleName()))
                .verifyComplete();
    }

}