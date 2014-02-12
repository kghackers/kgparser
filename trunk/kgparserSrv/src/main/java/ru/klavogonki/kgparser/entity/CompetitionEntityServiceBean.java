package ru.klavogonki.kgparser.entity;

import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Copyright 2014 LLC "Open Code"
 * http://www.o-code.ru
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
@Stateless
public class CompetitionEntityServiceBean implements CompetitionEntityService
{
	@Override
	public Long createCompetitionEntity(CompetitionEntity competitionEntity) {
		if (competitionEntity == null)
			return null;

		// todo: validate competitionEntity

		em.persist(competitionEntity);
		return competitionEntity.getId();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CompetitionEntity> getCompetitionEntities() {
		Query query = em.createQuery("select ce from CompetitionEntity ce order by ce.name"); // todo: exclude zipFileData loading
		return (List<CompetitionEntity>) query.getResultList();
	}

	@PersistenceContext
	private EntityManager em;

	private StringBuffer sb = new StringBuffer();

	private static final Logger logger = Logger.getLogger(CompetitionEntityServiceBean.class);
}