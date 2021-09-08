package com.ulegalize.lawfirm.repository.impl;

import com.ulegalize.lawfirm.repository.VirtualRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

@Repository
@Slf4j
public class VirtualRepositoryImpl implements VirtualRepository {
    @PersistenceContext
    protected EntityManager manager;

    public EntityManager getEntityManager() {
        return manager;
    }

    @Override
    public void setupVc(String tempVcKey, Long userId) {
        log.info("Entering setupVc {} and {}", tempVcKey, userId);

        EntityManager em = getEntityManager();
        StoredProcedureQuery storedProcedure = em.createStoredProcedureQuery("proctrans_createvc");
// set parameters
        storedProcedure.registerStoredProcedureParameter("cabname", String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("userid", Long.class, ParameterMode.IN);
        storedProcedure.setParameter("cabname", tempVcKey);
        storedProcedure.setParameter("userid", userId);
// execute SP
        storedProcedure.execute();

        getEntityManager().flush();
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("Entering repo deleteUser{}", userId);

        EntityManager em = getEntityManager();
        StoredProcedureQuery storedProcedure = em.createStoredProcedureQuery("delete_user");
// set parameters
        storedProcedure.registerStoredProcedureParameter("userid", Long.class, ParameterMode.IN);
        storedProcedure.setParameter("userid", userId);
// execute SP
        storedProcedure.execute();

        getEntityManager().flush();
        log.info("deleted done {}", userId);

    }

}
