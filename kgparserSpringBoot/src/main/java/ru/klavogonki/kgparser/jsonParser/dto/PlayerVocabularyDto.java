package ru.klavogonki.kgparser.jsonParser.dto;

import lombok.Data;
import ru.klavogonki.kgparser.Rank;
import ru.klavogonki.kgparser.excel.data.ExcelExportContextData;
import ru.klavogonki.kgparser.excel.data.OrderNumberExcelData;
import ru.klavogonki.kgparser.freemarker.OrderUtils;

import java.time.LocalDateTime;

/**
 * Объект для строки игрока в топах по словарям.
 *
 * @see ru.klavogonki.kgparser.jsonParser.entity.PlayerEntity
 * @see ru.klavogonki.kgparser.jsonParser.entity.PlayerVocabularyStatsEntity
 */
@Data
public class PlayerVocabularyDto
    implements OrderUtils.OrderNumberDto,
    ExcelExportContextData,
    OrderNumberExcelData
{

    /**
     * Порядковый номер в таблице результатов.
     */
    private String orderNumber;

    // данные игрока, PlayerEntity

    private Integer playerId; // KG user id

    private String login;

    private Rank rank;

    private Integer blocked;

    // string since Java8 Date/Time formatting does not work in Freemarker
    // see https://stackoverflow.com/questions/32063276/java-time-java-8-support-in-freemarker
    private String registered;

    private Integer ratingLevel;

    private String profileLink;

    private String vocabularyStatsLink;

    private String vocabularyStatsLinkWithoutHash;

    // данные игрока по конкретному словарю, PlayerVocabularyStatsEntity

    private Integer racesCount; // пробег игрока по словарю

    private Double averageSpeed; // средняя скорость игрока по словарю, знаков в минуту

    private Integer bestSpeed; // рекорд игрока по словарю, знаков в минуту

    private Double averageError; // процент ошибок игрока по словарю, в процентах

    private String haul; // время, проведённое игроком в словаре, в секундах. Отформатированное для показа в UI

    private Integer haulInteger; // true haul, for order number

    private Integer qual; // на сколько пройдена квалификация по словарю. Рекорды в словаре засчитываются в пределах 1.2 * qual.

    // don't care about dirty
    // string since Java8 Date/Time formatting does not work in Freemarker
    // see https://stackoverflow.com/questions/32063276/java-time-java-8-support-in-freemarker
    private String updated; // Время апдейта результата игроков по словарю. Московское время.

    // exact value of PlayerVocabularyStatsEntity#updated, for Excel
    private LocalDateTime updatedDateTime;

    private Boolean bookDone; // пройдена ли книга. Заполнено только для словарей-книг.
}
