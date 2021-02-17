/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Uros Zukancic
 */
@Stateless
public class IznajmljivanjeFacade extends AbstractFacade<Iznajmljivanje> {

    @PersistenceContext(unitName = "WebApplication1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public IznajmljivanjeFacade() {
        super(Iznajmljivanje.class);
    }
    
}
