package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TObjSharedWith;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TObjSharedWithRepository extends JpaRepository<TObjSharedWith, Long>, JpaSpecificationExecutor<TObjSharedWith> {
    @Query("delete from TObjSharedWith t where t.objId =?1 and t.toUserid = ?2 ")
    @Modifying
    void deleteByObjAndUserTo(Long objId, Long userTo);

    List<TObjSharedWith> findByObjId(Long objId);
}