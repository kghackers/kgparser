package ru.klavogonki.kgparser.jsonParser.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.klavogonki.kgparser.PlayerJsonData;
import ru.klavogonki.kgparser.jsonParser.PlayerIndexData;
import ru.klavogonki.kgparser.jsonParser.entity.PlayerEntity;
import ru.klavogonki.kgparser.util.DateUtils;

import java.time.LocalDateTime;

@Mapper
public interface PlayerMapper {

    // todo: add importDate?
    /* Yes, MapStruct works with public fields - a great library! */
    // PlayerSummary fields
    @Mapping(source = "summary.err", target = "getSummaryError")
    @Mapping(source = "summary.user.id", target = "playerId")
    @Mapping(source = "summary.user.login", target = "login")
    @Mapping(source = "summary.car.car", target = "car.id")
    @Mapping(source = "summary.car.color", target = "car.color")
    @Mapping(source = "summary.level", target = "rankLevel")
    @Mapping(source = "summary.title", target = "title")
    @Mapping(source = "summary.blocked", target = "blocked")

    // PlayerIndexData fields
    @Mapping(source = "indexData.err", target = "getIndexDataError")
    @Mapping(source = "indexData.stats.registered", target = "registered")
    @Mapping(source = "indexData.stats.achievementsCount", target = "achievementsCount")
    @Mapping(source = "indexData.stats.totalRacesCount", target = "totalRacesCount")
    @Mapping(source = "indexData.stats.bestSpeed", target = "bestSpeed")
    @Mapping(source = "indexData.stats.ratingLevel", target = "ratingLevel")
    @Mapping(source = "indexData.stats.friendsCount", target = "friendsCount")
    @Mapping(source = "indexData.stats.vocabulariesCount", target = "vocabulariesCount")
    @Mapping(source = "indexData.stats.carsCount", target = "carsCount")
    PlayerEntity playerJsonDataToPlayerEntity(PlayerJsonData data);

    default LocalDateTime registeredToLocalDateTime(PlayerIndexData.Registered registered) {
        return DateUtils.convertUserRegisteredTime(registered);
    }
}
