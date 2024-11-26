package ru.klavogonki.kgparser.servlet.model.round;

import ru.klavogonki.kgparser.Competition;
import ru.klavogonki.kgparser.Round;
import ru.klavogonki.kgparser.entity.CompetitionEntityService;
import su.opencode.kefir.srv.ClientException;
import su.opencode.kefir.web.Action;
import su.opencode.kefir.web.InitiableAction;
import su.opencode.kefir.web.JsonServlet;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public class RoundInfoGetServlet extends JsonServlet {

    @Override
    protected Action getAction() {
        return new InitiableAction() {
            @Override
            public void doAction() throws Exception {
                Long competitionEntityId = getLongParam(COMPETITION_ENTITY_ID_PARAM_NAME);
                if (competitionEntityId == null) {
                    String errorMessage = String.format("\"%s\" parameter is not set", COMPETITION_ENTITY_ID_PARAM_NAME);
                    throw new ClientException(errorMessage);
                }

                Integer roundNumber = getIntegerParam(ROUND_NUMBER_PARAM_NAME);
                if (roundNumber == null) {
                    String errorMessage = String.format("\"%s\" parameter is not set", ROUND_NUMBER_PARAM_NAME);
                    throw new ClientException(errorMessage);
                }

                CompetitionEntityService service = getService(CompetitionEntityService.class);
                Competition competition = service.getCompetition(competitionEntityId);

                if (competition == null) {
                    String errorMessage = String.format("Competition not found for competitionEntityId = %d", competitionEntityId);
                    throw new ClientException(errorMessage);
                }

                Round round = competition.getRound(roundNumber);
                if (round == null) {
                    String errorMessage = String.format("Round with number %d is not found for competitionEntityId = %d", roundNumber, competitionEntityId);
                    throw new ClientException(errorMessage);
                }

                RoundInfo roundInfo = new RoundInfo(competition, round);
                writeSuccess(roundInfo);
            }
        };
    }

    public static final String COMPETITION_ENTITY_ID_PARAM_NAME = "competitionId";
    public static final String ROUND_NUMBER_PARAM_NAME = "roundNumber";
}