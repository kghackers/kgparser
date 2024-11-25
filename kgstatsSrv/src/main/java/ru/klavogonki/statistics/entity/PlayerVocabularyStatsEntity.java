package ru.klavogonki.statistics.entity;

import lombok.Data;
import ru.klavogonki.common.DictionaryMode;
import ru.klavogonki.kgparser.NonStandardDictionaryType;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Player_Vocabulary_Stats")
public class PlayerVocabularyStatsEntity implements Serializable {
    @Id
//    @GeneratedValue
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pvs_SEQ")
    @SequenceGenerator(name = "pvs_SEQ", sequenceName = "pvs_SEQ", allocationSize = 1000)
    private Long dbId;

    private LocalDateTime importDate; // when PlayerDataDownloader has been executed

    // Caused by: org.hibernate.DuplicateMappingException: Table [player_vocabulary_stats] contains physical column name [player_id] referred to by multiple logical column names: [player_id], [playerId]
    // private Integer playerId; // KG user id

    @ManyToOne
//    @JoinColumn(name = "player_id", referencedColumnName = "player_id")
    @JoinColumn(nullable = false, name = "player_id", referencedColumnName = "playerId") // weird behaviour, db column name fails, entity field name is working.
    private PlayerEntity player;

    private String error; // also duplicated in PlayerEntity#getStatsOverviewError

    // todo: this can be extracted to a separate VocabularyEntity

    private String vocabularyCode;

    private Integer vocabularyId; // for non-standard dictionaries only

    private Integer vocabularyInfoId; // probably the db id from KG database, gametype[i].info.id

    private String vocabularyName;

    @Enumerated(EnumType.STRING)
    private NonStandardDictionaryType vocabularyType;

    private Integer vocabularySymbols; // for non-standard dictionaries only

    private Integer vocabularyRows; // for non-standard dictionaries only

    /**
     * <ul>
     *  <li>Для Обычного, Букв, Абракадабры, Яндекс.Рефератов, Цифр — {@code normal}</li>
     *  <li>Для Безошибочного — {@code noerror}</li>
     *  <li>Для Спринта — {@code sprint}</li>
     *  <li>Для Марафона — {@code marathon}</li>
     *  <li>Для нестандартных словарей — {@code normal}</li>
     * </ul>
     *
     * @see DictionaryMode#getDictionaryMode
     */
    @Enumerated(EnumType.STRING) // todo: save using the klavogonikiName field, NOT the enum itself
    private DictionaryMode vocabularyMode;

    /**
     * <ul>
     *  <li>Для Обычного, Безошибочного, Спринта, Марафона — {@code 0}</li>
     *  <li>Для Абракадабры — {@code -1}</li>
     *  <li>Для Цифр — {@code -2}</li>
     *  <li>Для Яндекс.Рефератов — {@code -3}</li>
     *  <li>Для Букв — {@code -4}</li>
     *  <li>Для нестандартных словарей — числовой id словаря.</li>
     * </ul>
     *
     * @see ru.klavogonki.common.DictionaryUtils#getTextType
     */
    private Integer vocabularyTextType;

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
    private Double averageError;

    /**
     * Время, проведённое игроком в словаре, в секундах
     */
    private Integer haul;

    /**
     * На сколько пройдена квалификация по словарю. Рекорды в словаре засчитываются в пределах 1.2 * qual.
     */
    private Integer qual;

    private Integer dirty; // todo: wtf is this?

    /**
     * Время апдейта результата игроков по словарю. Московское время.
     */
    private LocalDateTime updated;

    /**
     * Пройдена ли книга. Заполнено только для словарей-книг.
     */
    private Boolean bookDone;
}
