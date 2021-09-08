package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TSequences;
import com.ulegalize.lawfirm.model.enumeration.EnumSequenceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TSequenceRepository extends JpaRepository<TSequences, Long>, JpaSpecificationExecutor<TSequences> {
    @Query("select e from TSequences e where e.sequenceType = :sequenceType")
    public Optional<TSequences> maxSequenceById(EnumSequenceType sequenceType);
}