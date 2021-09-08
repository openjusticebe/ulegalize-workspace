package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TStripeSubscribers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TStripeSubscribersRepository extends JpaRepository<TStripeSubscribers, Integer>, JpaSpecificationExecutor<TStripeSubscribers> {

    public Optional<TStripeSubscribers> findByIdUser(Long userId);
}