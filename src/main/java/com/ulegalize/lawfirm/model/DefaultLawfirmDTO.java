package com.ulegalize.lawfirm.model;

import com.ulegalize.dto.ItemVatDTO;
import com.ulegalize.dto.LawfirmDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
public class DefaultLawfirmDTO {

    @Getter
    @Setter
    private String vcKey;
    @Getter
    @Setter
    private String language;
    @Setter
    @Getter
    private LawfirmDTO lawfirmDTO;
    @Setter
    @Getter
    private List<ItemVatDTO> itemVatDTOList;


}
