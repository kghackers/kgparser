package ru.klavogonki.kgparser.excel.player;

import ru.klavogonki.kgparser.jsonParser.dto.PlayerDto;

import java.util.function.Function;

public interface PlayerColumn<T> {

    String getColumnName();

    int getColumnWidth(); // in excel format for Sheet#setColumnWidth

    Function<PlayerDto, T> playerFieldGetter();

    Class<T> fieldClass();

    // todo: add excel cell format if required

    @SuppressWarnings("unchecked")
    default <T> T getValue(PlayerDto player) {
        return (T) playerFieldGetter().apply(player);
    }
}
