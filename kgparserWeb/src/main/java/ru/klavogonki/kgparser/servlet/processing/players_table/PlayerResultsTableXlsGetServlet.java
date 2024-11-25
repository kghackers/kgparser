package ru.klavogonki.kgparser.servlet.processing.players_table;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.klavogonki.kgparser.Competition;
import ru.klavogonki.kgparser.entity.CompetitionEntityService;
import ru.klavogonki.kgparser.processing.players_table.PlayersResultsTable;
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
public class PlayerResultsTableXlsGetServlet extends JsonServlet {

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

                CompetitionEntityService service = getService(CompetitionEntityService.class);
                Competition competition = service.getCompetition(competitionEntityId);

                if (competition == null) {
                    String errorMessage = String.format("Competition not found for competitionEntityId = %d", competitionEntityId);
                    throw new ClientException(errorMessage);
                }

                PlayersResultsTable table = new PlayersResultsTable();
                table.fillTable(competition);

                String fileName = String.format("competition-%s-playerResultsTable.xlsx", competitionEntityId);
                XSSFWorkbook workbook = PlayerResultsTableToXlsConverter.toXssfWorkbook(table);
                writeToExcel(fileName, workbook);
            }
        };
    }

    public static final String COMPETITION_ENTITY_ID_PARAM_NAME = "competitionId";
}
