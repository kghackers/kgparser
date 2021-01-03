package ru.klavogonki.kgparser.jsonParser.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ValueMapping;
import org.mapstruct.ValueMappings;
import ru.klavogonki.kgparser.DictionaryMode;
import ru.klavogonki.kgparser.NonStandardDictionaryType;
import ru.klavogonki.kgparser.jsonParser.entity.PlayerEntity;
import ru.klavogonki.kgparser.jsonParser.entity.PlayerVocabularyStatsEntity;
import ru.klavogonki.kgparser.util.DateUtils;
import ru.klavogonki.openapi.model.GetStatsOverviewGameType;
import ru.klavogonki.openapi.model.GetStatsOverviewResponse;
import ru.klavogonki.openapi.model.NonStandardVocabularyType;
import ru.klavogonki.openapi.model.VocabularyMode;

import java.time.LocalDateTime;

@Mapper
public interface PlayerVocabularyStatsMapper {

//    @Mapping(source = "", target = "vocabularyCode")
    @Mapping(source = "id", target = "vocabularyId")
    @Mapping(source = "name", target = "vocabularyName")
    @Mapping(source = "numRaces", target = "racesCount")
    @Mapping(source = "type", target = "vocabularyType")
    @Mapping(source = "symbols", target = "vocabularySymbols")
    @Mapping(source = "rows", target = "vocabularyRows")
    @Mapping(source = "bookDone", target = "bookDone")

    @Mapping(source = "info.id", target = "vocabularyInfoId")
//    @Mapping(source = "info.userId", target = "playerId") // todo: map to PlayerEntity
    @Mapping(source = "info.mode", target = "vocabularyMode")
    @Mapping(source = "info.texttype", target = "vocabularyTextType")
//    @Mapping(source = "info.numRaces", target = "") // we assume gameType.numRaces is the same, it is also validated
    @Mapping(source = "info.avgSpeed", target = "averageSpeed")
    @Mapping(source = "info.bestSpeed", target = "bestSpeed")
    @Mapping(source = "info.avgError", target = "averageError")
    @Mapping(source = "info.haul", target = "haul")
    @Mapping(source = "info.qual", target = "qual")
    @Mapping(source = "info.dirty", target = "dirty")
    @Mapping(source = "info.updated", target = "updated", dateFormat = DateUtils.DATE_TIME_FORMAT_FOR_UI)
    PlayerVocabularyStatsEntity statsGameTypeToEntity(
        GetStatsOverviewGameType gameType,
        @Context LocalDateTime importDate,
        @Context GetStatsOverviewResponse response,
        @Context String vocabularyCode,
        @Context PlayerEntity player
    );

    @ValueMappings({
        @ValueMapping(source = "WORDS", target = "words"),
        @ValueMapping(source = "PHRASES", target = "phrases"),
        @ValueMapping(source = "TEXTS", target = "texts"),
        @ValueMapping(source = "URL", target = "url"),
        @ValueMapping(source = "BOOK", target = "book"),
        @ValueMapping(source = "GENERATOR", target = "generator"),
    })
    NonStandardDictionaryType nonStandardDictionaryType(NonStandardVocabularyType vocabularyType);

    @ValueMappings({
        @ValueMapping(source = "NORMAL", target = "normal"),
        @ValueMapping(source = "NOERROR", target = "noerror"),
        @ValueMapping(source = "SPRINT", target = "sprint"),
        @ValueMapping(source = "MARATHON", target = "marathon"),
    })
    DictionaryMode dictionaryMode(VocabularyMode vocabularyMode);

    @AfterMapping
    default void fillContextData(
        GetStatsOverviewGameType gameType,
        @MappingTarget PlayerVocabularyStatsEntity entity,
        @Context LocalDateTime importDate,
        @Context GetStatsOverviewResponse response,
        @Context String vocabularyCode,
        @Context PlayerEntity player
    ) {
        entity.setImportDate(importDate);
        entity.setError(response.getErr());
        entity.setVocabularyCode(vocabularyCode);

/*
        // todo: do we need to set the whole playerEntity, at least with its dbId?
        PlayerEntity playerEntity = new PlayerEntity();
        playerEntity.setPlayerId(playerId);

*/
        entity.setPlayer(player);
    }
}
