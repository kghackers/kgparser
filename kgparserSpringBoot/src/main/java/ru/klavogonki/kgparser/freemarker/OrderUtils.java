package ru.klavogonki.kgparser.freemarker;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import ru.klavogonki.kgparser.jsonParser.dto.PlayerDto;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Log4j2
public class OrderUtils {

    public static String formatRange(Number from, Number to) {
        return String.format("%s–%s", from, to); // Ndash (Alt + 0150) used for number range, see https://www.artlebedev.ru/kovodstvo/sections/158/
    }

    public static <T> void fillOrderNumbers(List<PlayerDto> players, Function<PlayerDto, T> criteriaGetter) { // result can be null, therefore ToLongFunction does not work
        if (ObjectUtils.isEmpty(players)) {
            return;
        }

        int firstIndex = 0;
        int lastIndex = 0;

        int globalIndex = 0;
        int i = 0;

        while (globalIndex < players.size()) {
            T currentValue = criteriaGetter.apply(players.get(globalIndex));

            while (
                (i < players.size())
                    && Objects.equals(criteriaGetter.apply(players.get(i)), currentValue)
            ) {
                lastIndex = i;

                i++;
            }

            int firstPlace = firstIndex + 1;
            int lastPlace = lastIndex + 1;

            String orderNumber = (firstPlace == lastPlace) ? Integer.toString(firstPlace) : formatRange(firstPlace, lastPlace);

            for (int j = firstIndex; j <= lastIndex; j++) {
                PlayerDto player = players.get(j);
                player.setOrderNumber(orderNumber);
                logger.debug("Setting order number {} to player with login = \"{}\". Criteria value: {}.", orderNumber, player.getLogin(), criteriaGetter.apply(player));
            }

            globalIndex = i;
            firstIndex = i;
            lastIndex = i;
        }

        logger.debug("Set order numbers for {} players", players.size());
    }
}
