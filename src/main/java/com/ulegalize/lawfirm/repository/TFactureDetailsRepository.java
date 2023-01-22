package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.dto.GroupVatDTO;
import com.ulegalize.lawfirm.model.entity.TFactureDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TFactureDetailsRepository extends JpaRepository<TFactureDetails, Integer>, JpaSpecificationExecutor<TFactureDetails> {

    @Query(value = "select new com.ulegalize.lawfirm.model.dto.GroupVatDTO(fd.tva, (sum(fd.ttc) ) - ( sum(fd.htva) ))" +
            " from TFactureDetails fd" +
            " where fd.tFactures.idFacture= :idFacture" +
            " and fd.tva>0" +
            " group by fd.tva")
    List<GroupVatDTO> findItemVat(Long idFacture);

}