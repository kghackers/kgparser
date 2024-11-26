package ru.klavogonki.statistics.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import ru.klavogonki.common.Rank;
import ru.klavogonki.common.UrlConstructor;
import ru.klavogonki.statistics.dto.PlayerVocabularyDto;
import ru.klavogonki.statistics.entity.PlayerVocabularyStatsEntity;
import ru.klavogonki.statistics.freemarker.HaulUtils;
import ru.klavogonki.statistics.freemarker.OrderUtils;
import ru.klavogonki.statistics.util.DateUtils;

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

    @SuppressWarnings("squid:S4276") // OrderUtils.fillOrderNumbers will not work with ToIntFunction, it can fail on conversion of null to int.
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
