package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TJobs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TJobsRepository extends JpaRepository<TJobs, Long>, JpaSpecificationExecutor<TJobs> {

}