package ru.klavogonki.kgparser.jsonParser.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import ru.klavogonki.kgparser.Rank;
import ru.klavogonki.kgparser.freemarker.HaulUtils;
import ru.klavogonki.kgparser.freemarker.OrderUtils;
import ru.klavogonki.kgparser.http.UrlConstructor;
import ru.klavogonki.kgparser.jsonParser.dto.PlayerVocabularyDto;
import ru.klavogonki.kgparser.jsonParser.entity.PlayerVocabularyStatsEntity;
import ru.klavogonki.kgparser.util.DateUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@Mapper
public interface PlayerVocabularyDtoMapper {

    // fields from PlayerEntity
    @Mapping(source = "player.playerId", target = "playerId")
    @Mapping(source = "player.login", target = "login")
    @Mapping(source = "player.rankLevel", target = "rank")
    @Mapping(source = "player.blocked", target = "blocked")
    @Mapping(source = "player.registered", target = "registered")
    @Mapping(source = "player.ratingLevel", target = "ratingLevel")
    @Mapping(source = "player.profileLink", target = "profileLink")
    @Mapping(source = "entity", target = "vocabularyStatsLink", qualifiedByName = "vocabularyStatsLinkConverter")
    @Mapping(source = "entity", target = "vocabularyStatsLinkWithoutHash", qualifiedByName = "vocabularyStatsLinkWithoutHashConverter")

    // fields from PlayerVocabularyStatsEntity
    @Mapping(source = "haul", target = "haul", qualifiedByName = "haulConverter")
    @Mapping(source = "haul", target = "haulInteger")
    @Mapping(source = "updated", target = "updatedDateTime")
    @Mapping(source = "updated", target = "updated")
    PlayerVocabularyDto entityToDto(PlayerVocabularyStatsEntity entity);

    List<PlayerVocabularyDto> entitiesToDtos(List<PlayerVocabularyStatsEntity> entities, @Context Function<PlayerVocabularyDto, Integer> orderCriteriaGetter);

    default String localDateTimeToString(LocalDateTime localDateTime) {
        return DateUtils.formatDateTimeForUi(localDateTime);
    }

    default Rank rankLevelToRank(Integer rankLevel) {
        return Rank.getRank(rankLevel);
    }

    @Named("haulConverter")
    default String haulConverter(Integer haul) {
        return HaulUtils.format(haul);
    }

    @Named("vocabularyStatsLinkConverter")
    default String vocabularyStatsLinkConverter(PlayerVocabularyStatsEntity entity) {
        return UrlConstructor.userStatsByVocabulary(entity.getPlayer().getPlayerId(), entity.getVocabularyCode());
    }

    @Named("vocabularyStatsLinkWithoutHashConverter")
    default String vocabularyStatsLinkWithoutHashConverter(PlayerVocabularyStatsEntity entity) {
        return UrlConstructor.userStatsByVocabularyWithoutHash(entity.getPlayer().getPlayerId(), entity.getVocabularyCode());
    }

    @AfterMapping
    default void fillOrderNumbers(List<PlayerVocabularyStatsEntity> source, @MappingTarget List<PlayerVocabularyDto> target, @Context Function<PlayerVocabularyDto, Integer> orderCriteriaGetter) {
        OrderUtils.fillOrderNumbers(target, orderCriteriaGetter);
    }
}
