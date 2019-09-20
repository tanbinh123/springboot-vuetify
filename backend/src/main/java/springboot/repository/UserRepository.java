package springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import springboot.domain.entity.User;

import javax.validation.constraints.NotNull;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Nullable
    @Transactional
    @Query(value = "select u from User u where u.email = :email")
    User findUserByEmail(@Param("email") @NotNull String email);

    @Transactional
    @Query(value = "select case when count(u) > 0 then true else false end from User u where u.email = :email")
    boolean checkExistenceEmail(@Param("email") @NotNull String email);

    @Nullable
    @Transactional
    @Query(value = "select at.user from AccessToken at where at.token = :token")
    User findUserByToken(@Param("token") @NotNull String token);

    @Nullable
    @Transactional
    @Query(value = "select u from User u where u.email = :email and u.password = :password")
    User findUserByEmailAndPassword(@Param("email") @NotNull String email, @Param("password") @NotNull String password);

    @Transactional
    @Modifying
    @Query(value = "update User u set u.password = :password where u.email = :email")
    int updatePassword(String email, String password);

}
