package com.ulegalize.lawfirm.service.v2;

import com.ulegalize.dto.BankAccountDTO;

import java.util.List;

public interface RefCompteService {
    public List<BankAccountDTO> getAllBankAccount(String vcKey, Long userId);

    BankAccountDTO updateBankAccount(String vcKey, Long userId, Integer compteId, BankAccountDTO bankAccountDTO);

    Integer deleteBankAccount(String vcKey, Long userId, Integer compteId);

    Integer createBankAccount(String vcKey, Long userId, BankAccountDTO bankAccountDTO);

    BankAccountDTO getBankAccountById(String vcKey, Long userId, Integer compteId);
}
