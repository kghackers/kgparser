package ru.klavogonki.kgparser.jsonParser.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.klavogonki.kgparser.Rank;
import ru.klavogonki.kgparser.jsonParser.dto.PlayerDto;
import ru.klavogonki.kgparser.jsonParser.entity.PlayerEntity;
import ru.klavogonki.kgparser.util.DateUtils;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PlayerDtoMapper {

    /* todo: set orderNumber*/

    @Mapping(source = "rankLevel", target = "rank")
    PlayerDto playerEntityToPlayerDto(PlayerEntity entity);

    List<PlayerDto> playerEntitiesToPlayerDtos(List<PlayerEntity> entities);

    default String registeredToLocalDateTime(LocalDateTime registered) {
        return DateUtils.formatDateTimeForUi(registered);
    }

    default Rank rankLevelToRank(Integer rankLevel) {
        return Rank.getRank(rankLevel);
    }

    @AfterMapping
    default void fillOrderNumbers(List<PlayerEntity> source, @MappingTarget List<PlayerDto> target) {
        for (int i = 0; i < source.size(); i++) {
            target.get(i).setOrderNumber(i + 1);
        }

        // todo: @Log4j2 does not work, can we still use a logger?
//        System.out.printf("Filled order numbers for %d players.%n", source.size());
    }
}
