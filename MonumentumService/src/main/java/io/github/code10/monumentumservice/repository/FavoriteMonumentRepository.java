package io.github.code10.monumentumservice.repository;

import io.github.code10.monumentumservice.model.domain.Monument;
import io.github.code10.monumentumservice.model.domain.User;
import io.github.code10.monumentumservice.model.domain.UserFavoriteMonument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteMonumentRepository extends JpaRepository<UserFavoriteMonument, Long> {

    List<UserFavoriteMonument> findByUserOrderByMonumentDesc(User user);

    UserFavoriteMonument findByUserAndMonument(User user, Monument monument);
}
