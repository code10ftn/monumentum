package io.github.code10.monumentumservice.repository;

import io.github.code10.monumentumservice.model.domain.Monument;
import io.github.code10.monumentumservice.model.domain.UserCommentMonument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentMonumentRepository extends JpaRepository<UserCommentMonument, Long> {

    List<UserCommentMonument> findByMonumentOrderByTimestampDesc(Monument monument);
}
