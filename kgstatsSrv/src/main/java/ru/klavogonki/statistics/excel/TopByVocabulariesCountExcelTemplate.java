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

public class TopByVocabulariesCountExcelTemplate extends ExcelTemplate<PlayerDto> {

    @Override
    public String getSheetName() {
//        return String.format("Топ-%d по числу используемых словарей", players.size()); // will be cut to 31 character, freaking Excel!
        return String.format("Топ-%d по числу словарей", players.size()); // shorter name, to fit in Excel sheet name
    }

    @Override
    public List<? extends PlayerColumn<PlayerDto, ?>> getColumns() {
        return List.of(
            new OrderNumberColumn<>(),
            new LoginColumn<>(),
            new ProfileLinkColumn<>(),
            new VocabulariesCountColumn(),
            new BestSpeedColumn(),
            new TotalRacesCountColumn(),
            new RegisteredColumn(),
            new AchievementsCountColumn(),
            new RatingLevelColumn(),
            new FriendsCountColumn(),
            new CarsCountColumn()
        );
    }
}
