package com.example.webDemo3.repository;

import com.example.webDemo3.entity.Newsletter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

/**
 * lamnt98
 * 27/07
 */
@Repository
public interface NewsletterRepository extends JpaRepository<Newsletter,Integer> {
    @Query(value = "select n from Newsletter n where n.status = 0 and n.gim = 0")
    Page<Newsletter> loadAllLetter(Pageable paging);

    @Query(value = "select n from Newsletter n where (:header is NULL or n.header like %:header%) and n.status = 0 ")
    Page<Newsletter> searchLetterbyHeader(@Param("header") String header, Pageable paging);

    @Query(value = "select n from Newsletter n where (n.status = :status or :status is NULL ) "+
            " and (n.createDate = :createDate or :createDate is NULL ) "+
            " and n.userName like %:userName%")
    Page<Newsletter> findByStatusAndCreateDate(@Param("status") Integer status,
                                               @Param("createDate") Date createDate,
                                               @Param("userName") String userName ,Pageable paging);

    @Query(value = "select n from Newsletter n where n.gim = 1 ")
    List<Newsletter> findAllNewsletterGim();
}
