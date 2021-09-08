package com.ulegalize.lawfirm.service.v2;

import com.ulegalize.dto.LawyerDTO;

public interface LawyerV2Service {
    LawyerDTO getLawyer();

    Long updateLawyer(LawyerDTO lawyerDTO);

    Long updateLawyerLogo(byte[] bytes);
}
