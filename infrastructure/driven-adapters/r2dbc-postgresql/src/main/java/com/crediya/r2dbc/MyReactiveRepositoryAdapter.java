package com.crediya.r2dbc;

import com.crediya.model.user.User;
import com.crediya.model.user.gateways.UserRepository;
import com.crediya.r2dbc.entity.UserEntity;
import com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Repository
public class MyReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        User,
        UserEntity,
        Long,
        MyReactiveRepository
        > implements UserRepository {

    private final TransactionalOperator transactionalOperator;
    private final RoleReactiveRepository roleRepository;

    public MyReactiveRepositoryAdapter(
            MyReactiveRepository repository,
            RoleReactiveRepository roleRepository,
            ObjectMapper mapper,
            TransactionalOperator transactionalOperator
    ) {
        super(repository, mapper, d -> mapper.map(d, User.class));
        this.transactionalOperator = transactionalOperator;
        this.roleRepository = roleRepository;
    }

    @Override
    public Mono<User> getUserByEmail(String email) {
        return this.repository.findByEmail(email)
                .filter(Objects::nonNull)
                .flatMap(entity ->
                        roleRepository.findById(String.valueOf(entity.getRoleId()))
                                .map(roleEntity -> {
                                    User user = mapper.map(entity, User.class);
                                    user.setRoleName(roleEntity.getName());
                                    user.setEmail(email);
                                    return user;
                                })
                )
                .onErrorMap(e -> new RuntimeException("Error al consultar usuario por email", e));
    }

    @Override
    public Mono<User> saveUser(User user, Long roleId) {
        UserEntity entity = mapper.map(user, UserEntity.class);
        entity.setRoleId(roleId);
        return this.repository.save(entity)
                .map(saved -> {
                    user.setId(String.valueOf(saved.getId()));
                    user.setRoleName(user.getRoleName());
                    return user;
                })
                .onErrorMap(e -> new RuntimeException("Error al guardar usuario", e))
                .as(transactionalOperator::transactional);
    }

    @Override
    public Mono<User> findByIdentityDocument(String identityDocument) {
        return this.repository.findByIdentityDocument(identityDocument)
                .filter(Objects::nonNull)
                .map(entity -> mapper.map(entity, User.class));
    }

  @Override
  public Mono<Boolean> existsByEmail(String email) {
      return this.repository.existsByEmail(email)
              .onErrorMap(e -> {
                  return new RuntimeException("Error al verificar existencia de email", e);
              });
  }

    @Override
    public Flux<User> findUsersByIdentityDocument(List<String> identities) {
        return this.repository.findUsersByIdentityDocument(identities)
                .filter(Objects::nonNull)
                .map(entity -> mapper.map(entity, User.class))
                .onErrorMap(e -> new RuntimeException("Error al consultar usuarios por documentos de identidad", e));
    }
}