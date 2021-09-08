package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.VirtualcabClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VirtualcabClientRepository extends JpaRepository<VirtualcabClient, Long>, JpaSpecificationExecutor<VirtualcabClient> {

    @Query(value = "SELECT vcc from  VirtualcabClient vcc " +
            " join vcc.lawfirm lawfirm " +
            " where lawfirm.vckey = ?1 and vcc.tClients.f_company = ?1")
    List<VirtualcabClient> findByLawfirm(String vcKey);

    @Query(value = "SELECT vcc from  VirtualcabClient vcc " +
            " join vcc.lawfirm lawfirm " +
            " where lawfirm.vckey = ?1 and vcc.tClients.id_client = ?2")
    Optional<VirtualcabClient> findByLawfirmAAndTClients(String vcKey, Long clientId);
}