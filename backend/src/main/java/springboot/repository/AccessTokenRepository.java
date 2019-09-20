package springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import springboot.domain.entity.AccessToken;
import springboot.domain.entity.User;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {

    @Modifying
    @Transactional
    @Query(value = "delete from AccessToken at where at.user.id = :id and at.expirationDate < :currentDate")
    void deleteExpiredTokens(@Param("id") long userId, @Param("currentDate") @NotNull LocalDateTime localDateTime);

    @Nullable
    @Transactional
    @Query(value = "select at from AccessToken at where at.deviceId = :deviceId")
    AccessToken findAccessTokenByDeviceId(@Param("deviceId") @NotNull String deviceId);

    @Transactional
    @Query(value = "select case when count(at) > 0 then true else false end from AccessToken at where at.token = :token")
    boolean checkTokenExist(String token);

    @Transactional
    @Modifying
    @Query(value = "delete from AccessToken at where at.deviceId = :deviceId")
    void deleteTokenByDeviceId(String deviceId);
}
