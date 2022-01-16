package inu.market.notice.domain;

import inu.market.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Query("select n from Notice n order by n.id desc")
    List<Notice> findAll();
}
