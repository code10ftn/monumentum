package io.github.code10.monumentumservice.repository;

import io.github.code10.monumentumservice.model.domain.Monument;
import io.github.code10.monumentumservice.model.domain.User;
import io.github.code10.monumentumservice.model.domain.UserVisitedMonument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VisitedMonumentRepository extends JpaRepository<UserVisitedMonument, Long> {

    List<UserVisitedMonument> findByUserOrderByMonumentDesc(User user);

    UserVisitedMonument findByUserAndMonument(User user, Monument monument);
}
