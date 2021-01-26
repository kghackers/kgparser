package ru.klavogonki.statistics.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.klavogonki.kgparser.Rank;
import ru.klavogonki.statistics.dto.PlayerDto;
import ru.klavogonki.statistics.entity.PlayerEntity;
import ru.klavogonki.statistics.freemarker.OrderUtils;
import ru.klavogonki.statistics.util.DateUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@Mapper
public interface PlayerDtoMapper {

    @Mapping(source = "rankLevel", target = "rank")
    @Mapping(source = "registered", target = "registeredDateTime")
    @Mapping(source = "registered", target = "registered")
    PlayerDto playerEntityToPlayerDto(PlayerEntity entity);

    @SuppressWarnings("squid:S4276") // OrderUtils.fillOrderNumbers will not work with ToIntFunction, it can fail on conversion of null to int.
    List<PlayerDto> playerEntitiesToPlayerDtos(List<PlayerEntity> entities, @Context Function<PlayerDto, Integer> orderCriteriaGetter);

    default String registeredToLocalDateTime(LocalDateTime registered) {
        return DateUtils.formatDateTimeForUi(registered);
    }

    default Rank rankLevelToRank(Integer rankLevel) {
        return Rank.getRank(rankLevel);
    }

    @AfterMapping
    default void fillOrderNumbers(List<PlayerEntity> source, @MappingTarget List<PlayerDto> target, @Context Function<PlayerDto, Integer> orderCriteriaGetter) {
        OrderUtils.fillOrderNumbers(target, orderCriteriaGetter);
    }
}
