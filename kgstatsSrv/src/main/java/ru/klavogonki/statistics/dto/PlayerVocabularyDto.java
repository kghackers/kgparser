package ru.klavogonki.statistics.dto;

import lombok.Data;
import ru.klavogonki.kgparser.Rank;
import ru.klavogonki.statistics.excel.data.ExcelExportContextData;
import ru.klavogonki.statistics.excel.data.OrderNumberExcelData;
import ru.klavogonki.statistics.freemarker.OrderUtils;

import java.time.LocalDateTime;

/**
 * Объект для строки игрока в топах по словарям.
 *
 * @see ru.klavogonki.statistics.entity.PlayerEntity
 * @see ru.klavogonki.statistics.entity.PlayerVocabularyStatsEntity
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

    /**
     * Пробег игрока по словарю.
     */
    private Integer racesCount;

    /**
     * Средняя скорость игрока по словарю, знаков в минуту.
     */
    private Double averageSpeed;

    /**
     * Рекорд игрока по словарю, знаков в минуту.
     */
    private Integer bestSpeed;

    /**
     * Процент ошибок игрока по словарю, в процентах.
     */
    private Double averageError; //

    /**
     * Время, проведённое игроком в словаре, в секундах. Отформатированное для показа в UI.
     */
    private String haul;

    /**
     * True haul, for order number.
     */
    private Integer haulInteger;

    /**
     * На сколько пройдена квалификация по словарю. Рекорды в словаре засчитываются в пределах 1.2 * qual.
     */
    private Integer qual;

    // don't care about dirty
    // string since Java8 Date/Time formatting does not work in Freemarker
    // see https://stackoverflow.com/questions/32063276/java-time-java-8-support-in-freemarker
    /**
     * Время апдейта результата игроков по словарю. Московское время.
     */
    private String updated;

    /**
     * Exact value of PlayerVocabularyStatsEntity#updated, for Excel
     */
    private LocalDateTime updatedDateTime;

    /**
     * Пройдена ли книга. Заполнено только для словарей-книг.
     */
    private Boolean bookDone;
}
