package com.ulegalize.lawfirm.repository;

public interface VirtualRepository {
    public void setupVc(String tempVcKey, Long userId);

    public void deleteUser(Long userId);
}
