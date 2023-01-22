package com.ulegalize.lawfirm.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupVatDTO {
    BigDecimal vatVat;
    BigDecimal groupAmountVat;
}
