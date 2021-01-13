package ru.klavogonki.kgparser.excel;

import ru.klavogonki.kgparser.excel.player.AchievementsCountColumn;
import ru.klavogonki.kgparser.excel.player.BestSpeedColumn;
import ru.klavogonki.kgparser.excel.player.CarsCountColumn;
import ru.klavogonki.kgparser.excel.player.FriendsCountColumn;
import ru.klavogonki.kgparser.excel.player.LoginColumn;
import ru.klavogonki.kgparser.excel.player.OrderNumberColumn;
import ru.klavogonki.kgparser.excel.player.PlayerColumn;
import ru.klavogonki.kgparser.excel.player.ProfileLinkColumn;
import ru.klavogonki.kgparser.excel.player.RatingLevelColumn;
import ru.klavogonki.kgparser.excel.player.RegisteredColumn;
import ru.klavogonki.kgparser.excel.player.TotalRacesCountColumn;
import ru.klavogonki.kgparser.excel.player.VocabulariesCountColumn;
import ru.klavogonki.kgparser.jsonParser.dto.PlayerDto;

import java.util.List;

public class TopByRatingLevelExcelTemplate extends ExcelTemplate<PlayerDto> {

    @Override
    public String getSheetName() {
        return String.format("Топ-%d по рейтинговому уровню", players.size());
    }

    @Override
    public List<? extends PlayerColumn<PlayerDto, ?>> getColumns() {
        return List.of(
            new OrderNumberColumn<>(),
            new LoginColumn<>(),
            new ProfileLinkColumn<>(),
            new RatingLevelColumn(),
            new BestSpeedColumn(),
            new TotalRacesCountColumn(),
            new RegisteredColumn(),
            new AchievementsCountColumn(),
            new FriendsCountColumn(),
            new VocabulariesCountColumn(),
            new CarsCountColumn()
        );
    }
}
