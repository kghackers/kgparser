package ru.klavogonki.kgparser.jsonParser.repository;

import org.springframework.data.repository.CrudRepository;
import ru.klavogonki.kgparser.jsonParser.entity.PlayerEntity;

import java.util.List;

public interface PlayerRepository extends CrudRepository<PlayerEntity, Long> {

    List<PlayerEntity> findByPlayerId(int playerId);
}
