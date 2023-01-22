package com.ulegalize.lawfirm.repository;

import com.ulegalize.dto.ItemLongDto;
import com.ulegalize.lawfirm.model.entity.TVirtualCabTags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface VirtualcabTagsRepository extends JpaRepository<TVirtualCabTags, Long> {

    @Query("select new com.ulegalize.dto.ItemLongDto(tvct.id, tvct.label)" +
            " from TVirtualCabTags tvct" +
            " where tvct.lawfirmEntity.vckey = :vckey" +
            " and lower(tvct.label) like lower(concat('%', :searchCriteria, '%' )) ")
    List<ItemLongDto> findTVirtualCabTagsBySearchCriteria(String vckey, String searchCriteria);

    @Query(value = "select tvct" +
            " from TVirtualCabTags tvct " +
            " where tvct.label = ?1 " +
            " and tvct.lawfirmEntity.vckey = ?2 ")
    Optional<TVirtualCabTags> findTVirtualCabTagsByLabel(String label, String vckey);
}
