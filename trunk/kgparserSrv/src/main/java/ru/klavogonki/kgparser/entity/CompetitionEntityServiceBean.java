package ru.klavogonki.kgparser.entity;

import org.apache.log4j.Logger;
import su.opencode.kefir.util.SqlUtils;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
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
		Query query = em.createQuery("select ce.id, ce.name, ce.link, ce.comment, ce.zipFileName, ce.zipFileSize, ce.zipFileContentType from CompetitionEntity ce order by ce.name");
		List resultList = query.getResultList();

		List<CompetitionEntity> result = new ArrayList<>();
		for (Object o : resultList)
		{
			Object[] values = (Object[]) o;

			Long id = SqlUtils.getLong( values[0] );
			String name = SqlUtils.getString( values[1] );
			String link = SqlUtils.getString( values[2] );
			String comment = SqlUtils.getString( values[3] );

			String zipFileName = SqlUtils.getString( values[4] );
			Long zipFileSize = SqlUtils.getLong( values[5] );
			String zipFileContentType = SqlUtils.getString( values[6] );

			CompetitionEntity entity = new CompetitionEntity();
			entity.setId(id);
			entity.setName(name);
			entity.setLink(link);
			entity.setComment(comment);

			entity.setZipFileName(zipFileName);
			entity.setZipFileSize(zipFileSize);
			entity.setZipFileContentType(zipFileContentType);

			result.add(entity);
		}

		return result;
	}

	@Override
	public CompetitionEntity getCompetitionEntity(Long id) {
		if (id == null)
			return null;

		return em.find(CompetitionEntity.class, id);
	}

	@PersistenceContext
	private EntityManager em;

	private StringBuffer sb = new StringBuffer();

	private static final Logger logger = Logger.getLogger(CompetitionEntityServiceBean.class);
}