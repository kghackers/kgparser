package ru.klavogonki.kgparser.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.klavogonki.kgparser.Competition;
import su.opencode.kefir.srv.json.JsonObject;
import su.opencode.kefir.util.SqlUtils;
import su.opencode.kefir.util.StringUtils;

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

	@Override
	public Competition getCompetition(Long competitionEntityId) {
		if (competitionEntityId == null)
			return null;

		CompetitionEntity entity = getCompetitionEntity(competitionEntityId);
		if (entity == null)
			return null;

		String competitionJson = entity.getCompetitionJson();
		if ( StringUtils.empty(competitionJson) )
			return null;

		try
		{
			Competition competition = JsonObject.fromJson(competitionJson, Competition.class);
			// todo: fill transient data if needed
			return competition;
		}
		catch (Exception e)
		{
			String errorMessage = String.format("Error while parsing competitionJson for CompetitionEntity with id = %d", competitionEntityId);
			logger.error(errorMessage, e);
			throw new RuntimeException(errorMessage, e);
		}
	}

	@PersistenceContext
	private EntityManager em;

	private static final Logger logger = LogManager.getLogger(CompetitionEntityServiceBean.class);
}
