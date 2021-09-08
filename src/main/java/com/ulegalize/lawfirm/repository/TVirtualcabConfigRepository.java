package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TVirtualcabConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
@Repository
public interface TVirtualcabConfigRepository
        extends JpaRepository<TVirtualcabConfig, String>, JpaSpecificationExecutor<TVirtualcabConfig> {
    public List<TVirtualcabConfig> findAllByVcKey(String vcKey);

    @Query(value = "DELETE from  TVirtualcabConfig l where upper(l.vcKey) = ?1 and l.description like concat('%', ?2 , '%')")
    public void removeByVcKeyAndDescription(String vcKey, String description);

    @Query(value = "SELECT l from TVirtualcabConfig l where upper(l.vcKey) = ?1 and l.description like concat('%', ?2 , '%')")
    TVirtualcabConfig findLawfirmConfigInfoByVcKeyAndDescription(String vcKey, String lawfirmConfigDescription);

}