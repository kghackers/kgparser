package ru.klavogonki.statistics.excel;

import ru.klavogonki.statistics.excel.player.AchievementsCountColumn;
import ru.klavogonki.statistics.excel.player.BestSpeedColumn;
import ru.klavogonki.statistics.excel.player.CarsCountColumn;
import ru.klavogonki.statistics.excel.player.FriendsCountColumn;
import ru.klavogonki.statistics.excel.player.LoginColumn;
import ru.klavogonki.statistics.excel.player.OrderNumberColumn;
import ru.klavogonki.statistics.excel.player.PlayerColumn;
import ru.klavogonki.statistics.excel.player.ProfileLinkColumn;
import ru.klavogonki.statistics.excel.player.RatingLevelColumn;
import ru.klavogonki.statistics.excel.player.RegisteredColumn;
import ru.klavogonki.statistics.excel.player.TotalRacesCountColumn;
import ru.klavogonki.statistics.excel.player.VocabulariesCountColumn;
import ru.klavogonki.statistics.dto.PlayerDto;

import java.util.List;

public class TopByTotalRacesCountExcelTemplate extends ExcelTemplate<PlayerDto> {

    @Override
    public String getSheetName() {
        return String.format("Топ-%d по общему пробегу", players.size());
    }

    @Override
    public List<? extends PlayerColumn<PlayerDto, ?>> getColumns() {
        return List.of(
            new OrderNumberColumn<>(),
            new LoginColumn<>(),
            new ProfileLinkColumn<>(),
            new TotalRacesCountColumn(),
            new BestSpeedColumn(),
            new RegisteredColumn(),
            new AchievementsCountColumn(),
            new RatingLevelColumn(),
            new FriendsCountColumn(),
            new VocabulariesCountColumn(),
            new CarsCountColumn()
        );
    }
}
