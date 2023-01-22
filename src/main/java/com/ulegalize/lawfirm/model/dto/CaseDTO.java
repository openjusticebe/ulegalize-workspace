package com.ulegalize.lawfirm.model.dto;

import com.ulegalize.dto.ItemPartieDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaseDTO implements Serializable {

    private String id;
    private List<ItemPartieDTO> partieEmail;
    private LocalDateTime createDate;
    private String createUser;
    private LocalDateTime updateDate;
    private String updateUser;

}
