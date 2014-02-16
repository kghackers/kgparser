package ru.klavogonki.kgparser.servlet.model.highchart;

import ru.klavogonki.kgparser.Competition;
import ru.klavogonki.kgparser.entity.CompetitionEntityService;
import ru.klavogonki.kgparser.processing.ErrorsCountChartFiller;
import ru.klavogonki.kgparser.processing.HighChartValue;
import su.opencode.kefir.srv.ClientException;
import su.opencode.kefir.web.Action;
import su.opencode.kefir.web.InitiableAction;
import su.opencode.kefir.web.JsonServlet;

import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public class ErrorsCountChartValuesGetServlet extends JsonServlet
{
	@Override
	protected Action getAction() {
		return new InitiableAction()
		{
			@Override
			public void doAction() throws Exception {
				Long competitionEntityId = getLongParam(COMPETITION_ENTITY_ID_PARAM_NAME);
				if (competitionEntityId == null)
					throw new ClientException( concat(sb, "\"", COMPETITION_ENTITY_ID_PARAM_NAME, "\" parameter is not set") );

				CompetitionEntityService service = getService(CompetitionEntityService.class);
				Competition competition = service.getCompetition(competitionEntityId);

				if (competition == null)
					throw new ClientException( concat(sb, "Competition not found for competitionEntityId = ", competitionEntityId) );

				HighChartValue highChartValue = ErrorsCountChartFiller.fillData(competition);
				writeSuccess(highChartValue);
			}
		};
	}

	public static final String COMPETITION_ENTITY_ID_PARAM_NAME = "competitionId";
}