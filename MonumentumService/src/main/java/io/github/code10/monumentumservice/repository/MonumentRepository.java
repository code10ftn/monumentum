package io.github.code10.monumentumservice.repository;

import io.github.code10.monumentumservice.model.domain.Monument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MonumentRepository extends JpaRepository<Monument, Long> {

    List<Monument> findAllByOrderByIdDesc();

    Optional<Monument> findById(long id);
}
