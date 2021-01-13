package ru.klavogonki.kgparser.jsonParser.dto;

import lombok.Data;
import ru.klavogonki.kgparser.Rank;
import ru.klavogonki.kgparser.excel.data.ExcelExportContextData;
import ru.klavogonki.kgparser.excel.data.OrderNumberExcelData;
import ru.klavogonki.kgparser.freemarker.OrderUtils;

import java.time.LocalDateTime;

@Data
public class PlayerDto
    implements
    OrderUtils.OrderNumberDto,
    ExcelExportContextData,
    OrderNumberExcelData
{ // object to use in Freemarker templates

    private String orderNumber; // to display in table

    private Long dbId;

    // string since Java8 Date/Time formatting does not work in Freemarker
    // see https://stackoverflow.com/questions/32063276/java-time-java-8-support-in-freemarker
    private String importDate; // when PlayerDataDownloader has been executed

    private String getSummaryError; // we import all users, including non-existing

    private String getIndexDataError; // we import all users, including non-existing and users with failed /get-indexData

    // fields from PlayerSummary
    private Integer playerId; // KG user id

    private String login;

    private CarDto car; // todo: ca also map to Car to have access to its fields

    // don't care about isOnline

    private Rank rank;

    private String title;

    private Integer blocked;

    // fields from PlayerIndexData
    // don't care for PlayerIndexData.bio for now
    // todo: add bio data if ever required

    // PlayerIndexData.stats

    // string since Java8 Date/Time formatting does not work in Freemarker
    // see https://stackoverflow.com/questions/32063276/java-time-java-8-support-in-freemarker
    private String registered;

    // exact value of PlayerEntity#registered, for Excel
    private LocalDateTime registeredDateTime;

    private Integer achievementsCount;

    private Integer totalRacesCount;

    private Integer bestSpeed;

    private Integer ratingLevel;

    private Integer friendsCount;

    private Integer vocabulariesCount;

    private Integer carsCount;

    private String profileLink;
}
