package com.ulegalize.lawfirm.repository;

import com.ulegalize.dto.BankAccountDTO;
import com.ulegalize.dto.ItemDto;
import com.ulegalize.enumeration.EnumAccountType;
import com.ulegalize.lawfirm.model.entity.RefCompte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RefCompteRepository extends JpaRepository<RefCompte, Integer>, JpaSpecificationExecutor<RefCompte> {

    @Query("select new com.ulegalize.dto.ItemDto(r.idCompte, CONCAT(r.compteNum, ' - ', r.compteRef )) " +
            "from RefCompte r " +
            " where r.vcKey = ?1" +
            " and r.archived = false " +
            " order by r.compteNum")
    List<ItemDto> findAllOrderBy(String vcKey);

    @Query("select new com.ulegalize.dto.BankAccountDTO(m.idCompte , m.vcKey, m.compteNum, " +
            "m.compteRef, m.userUpd, m.dateUpd, m.archived, m.accountTypeId) " +
            " from RefCompte m" +
            " where m.vcKey = :vcKey order by m.compteRef desc")
    List<BankAccountDTO> findAllItemByVcKey(String vcKey);

    @Query("select new com.ulegalize.dto.BankAccountDTO(m.idCompte , m.vcKey, m.compteNum, " +
            "m.compteRef, m.userUpd, m.dateUpd, m.archived, m.accountTypeId) " +
            " from RefCompte m" +
            " where m.vcKey = :vcKey and m.idCompte = :compteId")
    BankAccountDTO findDTOById(String vcKey, Integer compteId);

    @Query("select new com.ulegalize.dto.ItemDto(r.idCompte, CONCAT(r.compteNum, ' - ', r.compteRef )) " +
            " from RefCompte r" +
            " where r.vcKey = :vcKey " +
            " and r.accountTypeId = :accountType " +
            " and r.archived = false" +
            " order by r.compteNum")
    List<ItemDto> findDTOByIdAndAccountTypeId(String vcKey, EnumAccountType accountType);
}