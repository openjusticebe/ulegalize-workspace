package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TVcGroupment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TVcGroupmentRepository extends CrudRepository<TVcGroupment, Integer>, JpaSpecificationExecutor<TVcGroupment> {

    Long deleteByVcKey(String vcKey);

    List<TVcGroupment> findAllByVcKey(String vcKey);
}