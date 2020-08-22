package com.example.webDemo3.repository;

import com.example.webDemo3.entity.Class;
import com.example.webDemo3.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<Class,Integer> {
    @Query(value = "select c from Class c order by c.grade")
    List<Class> findAll();

    @Query(value = "select c from Class c where c.giftedClass.giftedClassId = :giftedClassId order by c.giftedClass.giftedClassId asc")
    List<Class> findByGifted(@Param("giftedClassId")Integer giftedClassId);

    Class findByClassId(Integer classId);

    List<Class> findClassListByClassIdentifier(String classIdentifier);

    @Query(value = "select c from Class c where c.classIdentifier like %:classIdentifier% and c.grade = :roleId")
    Page<Class> searchClassByCondition(@Param("classIdentifier") String classIdentifier, @Param("roleId") Integer roleId, Pageable paging);

    @Query(value = "select c from Class c where c.classIdentifier like %:classIdentifier% and c.grade = :roleId and (c.status <> 1 or c.status is null)")
    Page<Class> searchActiveClassByCondition(@Param("classIdentifier") String classIdentifier, @Param("roleId") Integer roleId, Pageable paging);

    @Query(value = "select c from Class c where c.classIdentifier like %:classIdentifier% and c.grade = :roleId and (c.status = 1 and c.status is not null)")
    Page<Class> searchInactiveClassByCondition(@Param("classIdentifier") String classIdentifier, @Param("roleId") Integer roleId, Pageable paging);

    @Query(value = "select c from Class c where c.classIdentifier like %:classIdentifier%")
    Page<Class> searchClassByClassIdentifier(@Param("classIdentifier") String classIdentifier, Pageable paging);

    @Query(value = "select c from Class c where c.classIdentifier like %:classIdentifier% and (c.status <> 1 or c.status is null)")
    Page<Class> searchActiveClassByClassIdentifier(@Param("classIdentifier") String classIdentifier, Pageable paging);

    @Query(value = "select c from Class c where c.classIdentifier like %:classIdentifier% and (c.status = 1 and c.status is not null)")
    Page<Class> searchInactiveClassByClassIdentifier(@Param("classIdentifier") String classIdentifier, Pageable paging);

    @Query(value = "select c from Class c where c.grade = :grade and c.giftedClass.giftedClassId = :giftedName ")
    Class searchClassByGradeAndGifedId(@Param("grade") Integer classIdentifier,@Param("giftedName") Integer giftedId);

    @Query(value = "select c from Class c where c.classIdentifier = :classIdentifier and (c.status <> 1 or c.status is null)")
    Class findClassActiveByClassIdentifier(@Param("classIdentifier") String classIdentifier);

    @Query(value = "select c from Class c order by c.grade, c.giftedClass.giftedClassId asc")
    List<Class> findAllOrderByClassName();
}
