package io.github.code10.monumentumservice.repository;

import io.github.code10.monumentumservice.model.domain.Monument;
import io.github.code10.monumentumservice.model.domain.User;
import io.github.code10.monumentumservice.model.domain.UserRatedMonument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatedMonumentRepository extends JpaRepository<UserRatedMonument, Long> {

    UserRatedMonument findByUserAndMonument(User user, Monument monument);
}
