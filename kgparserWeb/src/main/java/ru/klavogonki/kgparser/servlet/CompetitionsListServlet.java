package ru.klavogonki.kgparser.servlet;

import ru.klavogonki.kgparser.entity.CompetitionEntity;
import ru.klavogonki.kgparser.entity.CompetitionEntityService;
import su.opencode.kefir.web.Action;
import su.opencode.kefir.web.InitiableAction;
import su.opencode.kefir.web.JsonServlet;

import java.util.List;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public class CompetitionsListServlet extends JsonServlet
{
	@Override
	protected Action getAction() {
		return new InitiableAction()
		{
			@Override
			public void doAction() throws Exception {
				CompetitionEntityService service = getService(CompetitionEntityService.class);
				List<CompetitionEntity> competitionEntities = service.getCompetitionEntities();

				writeSuccess(competitionEntities);
			}
		};
	}
}