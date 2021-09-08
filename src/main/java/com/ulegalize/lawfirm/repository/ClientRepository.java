package com.ulegalize.lawfirm.repository;

import com.ulegalize.enumeration.EnumClientType;
import com.ulegalize.lawfirm.model.entity.TClients;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<TClients, Long> {
    @Query(value = "SELECT d from TClients d " +
            "join fetch d.virtualcabClientList vcc " +
            "join vcc.lawfirm lawfirm " +
            "join lawfirm.lawfirmUsers lu " +
            "join lu.user user " +
            "where (lawfirm.vckey = vcc.lawfirm.vckey or user.id = d.user_id)" +
            " and user.aliasPublic = :aliasPublic and d.f_email = :email")
    List<TClients> findByAliasPublic(String aliasPublic, String email);

    @Query(value = "SELECT d from TClients d" +
            " join fetch d.virtualcabClientList vcc " +
            " join vcc.lawfirm lawfirm " +
            " where lawfirm.vckey in :vcKeys or d.user_id = :userId")
    List<TClients> findByUserIdOrVcKey(List<String> vcKeys, Long userId);

    @Query(value = "SELECT d from TClients d " +
            " join d.virtualcabClientList vcc " +
            " join vcc.lawfirm lawfirm " +
            " where (lawfirm.vckey in :vcKeys or d.user_id = :userId) and d.f_email = :email")
    List<TClients> findByUserIdOrVcKeyAAndF_email(List<String> vcKeys, Long userId, String email);

    @Query(value = "SELECT distinct client from TClients client " +
            " join fetch client.virtualcabClientList vcc " +
            " join vcc.lawfirm lawfirm " +
            " where (lawfirm.vckey in :vcKey or client.user_id = :userId) " +
            " and (client.f_nom like %:searchCriteria%" +
            " or client.f_prenom like %:searchCriteria% or client.f_company like %:searchCriteria%)")
    List<TClients> findByUserIdOrVcKeyWithPagination(String vcKey, Long userId, String searchCriteria, Pageable pageable);

    @Query(value = "SELECT count(client) from TClients client " +
            " join client.virtualcabClientList vcc " +
            " join vcc.lawfirm lawfirm " +
            " where (lawfirm.vckey in :vcKey or client.user_id = :userId) " +
            " and (client.f_nom like %:searchCriteria% " +
            " or client.f_prenom like %:searchCriteria% or client.f_company like %:searchCriteria%)")
    Long countByUserIdOrVcKeyWithPagination(String vcKey, Long userId, String searchCriteria);

    @Query(value = "SELECT d from TClients d " +
            " join fetch d.virtualcabClientList vcc " +
            " join vcc.lawfirm lawfirm " +
            " where d.id_client = :clientId " +
            " and (lawfirm.vckey = :vcKey or d.user_id = :userId)")
    Optional<TClients> findById_clientAndUserIdOrVcKey(Long clientId, String vcKey, Long userId);

    @Query(value = "SELECT count(d) from TClients d " +
            " join d.virtualcabClientList vcc " +
            " join vcc.lawfirm lawfirm " +
            " where lawfirm.vckey = :vcKey or d.user_id = :userId")
    Long countByUserIdOrVcKey(String vcKey, Long userId);

    @Query(value = "SELECT count(d) from TClients d " +
            " join d.virtualcabClientList vcc " +
            " join vcc.lawfirm lawfirm " +
            " where lawfirm.vckey = ?1 and d.client_type = ?2 and d.f_company = ?3")
    Long countByVcKeyAndClientTypeAndFCompany(String vc_key, EnumClientType ca, String vcKeyClient);

    @Query(value = "SELECT d from TClients d " +
            " join fetch d.virtualcabClientList vcc " +
            " join vcc.lawfirm lawfirm " +
            " where d.id_client in :clientIds " +
            " and (lawfirm.vckey = :vcKey or d.user_id = :userId)")
    List<TClients> findByIds(List<Long> clientIds, String vcKey, Long userId);
}
