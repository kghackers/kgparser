package ru.klavogonki.kgparser.jsonParser.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.klavogonki.kgparser.PlayerJsonData;
import ru.klavogonki.kgparser.jsonParser.entity.PlayerEntity;
import ru.klavogonki.kgparser.util.DateUtils;
import ru.klavogonki.openapi.model.Microtime;

import java.time.LocalDateTime;

@Mapper
public interface PlayerMapper {

    // todo: add importDate?
    /* Yes, MapStruct works with public fields - a great library! */
    // GetSummaryResponse fields
    @Mapping(source = "summary.err", target = "getSummaryError")
    @Mapping(source = "summary.user.id", target = "playerId")
    @Mapping(source = "summary.user.login", target = "login")
    @Mapping(source = "summary.car.car", target = "car.id")
    @Mapping(source = "summary.car.color", target = "car.color")
    @Mapping(source = "summary.level", target = "rankLevel")
    @Mapping(source = "summary.title", target = "title")
    @Mapping(source = "summary.blocked", target = "blocked")

    // GetIndexDataResponse fields
    @Mapping(source = "indexData.err", target = "getIndexDataError")
    @Mapping(source = "indexData.stats.registered", target = "registered")
    @Mapping(source = "indexData.stats.achievesCnt", target = "achievementsCount")
    @Mapping(source = "indexData.stats.totalNumRaces", target = "totalRacesCount")
    @Mapping(source = "indexData.stats.bestSpeed", target = "bestSpeed")
    @Mapping(source = "indexData.stats.ratingLevel", target = "ratingLevel")
    @Mapping(source = "indexData.stats.friendsCnt", target = "friendsCount")
    @Mapping(source = "indexData.stats.vocsCnt", target = "vocabulariesCount")
    @Mapping(source = "indexData.stats.carsCnt", target = "carsCount")

    // GetStatsOverviewResponse fields
    @Mapping(source = "statsOverview.err", target = "getStatsOverviewError")
    // other fields will be parsed to PlayerVocabularyStatsEntity in a separate parser

    PlayerEntity playerJsonDataToPlayerEntity(PlayerJsonData data);

    default LocalDateTime registeredToLocalDateTime(Microtime registered) {
        return DateUtils.convertUserRegisteredTime(registered);
    }
}
