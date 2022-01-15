package inu.market.major.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MajorRepository extends JpaRepository<Major, Long> {

    Optional<Major> findByName(String name);

    List<Major> findByParentIsNull();

    @Query("select m from Major m where m.parent.id =:parentId")
    List<Major> findByParentId(@Param("parentId") Long parentId);
}
