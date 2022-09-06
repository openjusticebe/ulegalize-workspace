package com.ulegalize.lawfirm.model.converter;

import com.ulegalize.dto.ComptaDTO;
import com.ulegalize.enumeration.EnumRefTransaction;
import com.ulegalize.enumeration.EnumTType;
import com.ulegalize.lawfirm.model.entity.TFrais;
import com.ulegalize.lawfirm.utils.SuperTriConverter;
import org.springframework.stereotype.Component;

@Component
public class DTOToFraisEntityConverter implements SuperTriConverter<ComptaDTO, TFrais, TFrais> {

    @Override
    public TFrais apply(ComptaDTO dto, TFrais tFrais) {

        tFrais.setIdFrais(dto.getId());
        tFrais.setVcKey(dto.getVcKey());
        tFrais.setGridId(dto.getGridId());
        tFrais.setIdCompte(dto.getIdCompte());
        tFrais.setIdDoss(dto.getIdDoss());
        tFrais.setIdFacture(dto.getIdFacture());
        tFrais.setIdClient(dto.getIdUser());
        tFrais.setMontant(dto.getMontant());
        tFrais.setMemo(dto.getMemo() != null ? dto.getMemo() : "");
        tFrais.setIdPoste(dto.getIdPost());
        tFrais.setIdTransaction(EnumRefTransaction.fromId(dto.getIdTransaction()));
        tFrais.setIdType(EnumTType.fromId(dto.getIdType()));
        tFrais.setMontantht(dto.getMontantHt());
        tFrais.setRatio(dto.getRatio());
        tFrais.setDateValue(dto.getDateValue());
        return tFrais;

    }
}
