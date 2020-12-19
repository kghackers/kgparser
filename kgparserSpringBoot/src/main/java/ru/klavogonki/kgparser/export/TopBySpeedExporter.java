package ru.klavogonki.kgparser.export;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.klavogonki.kgparser.freemarker.LoginToPageTemplate;
import ru.klavogonki.kgparser.freemarker.PageUrls;
import ru.klavogonki.kgparser.freemarker.TopBySpeedPageTemplate;
import ru.klavogonki.kgparser.jsonParser.JacksonUtils;
import ru.klavogonki.kgparser.jsonParser.dto.PlayerDto;
import ru.klavogonki.kgparser.jsonParser.entity.PlayerEntity;
import ru.klavogonki.kgparser.jsonParser.mapper.PlayerDtoMapper;
import ru.klavogonki.kgparser.jsonParser.repository.PlayerRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Component
public class TopBySpeedExporter {
    private static final int TOTAL_RACES_COUNT_MIN = 1000;
    private static final int PAGE_SIZE = 100;

    @Autowired
    private PlayerRepository playerRepository;

    // todo: autowire it, @see https://mapstruct.org/documentation/stable/reference/html/#using-dependency-injection
    private final PlayerDtoMapper mapper = Mappers.getMapper(PlayerDtoMapper.class);

    public void export() {
        // todo: get rootDir from config
        final String rootDir = "C:/java/kgparser/kgparserWeb/src/main/webapp/";

        // !!! todo: !user paged query instead of manually splitting the result to pages
        List<PlayerEntity> players = playerRepository.findByTotalRacesCountGreaterThanEqualAndBlockedEqualsOrderByBestSpeedDescTotalRacesCountDesc(
            TOTAL_RACES_COUNT_MIN,
            0
        );

        List<PlayerDto> dtos = mapper.playerEntitiesToPlayerDtos(players);

        int totalPlayers = players.size();
        logger.debug("Total players: {}", totalPlayers);

        int totalPages = getPagesCount(totalPlayers, PAGE_SIZE);
        logger.debug("Total records: {}. Page size: {}. Total pages: {}", totalPlayers, PAGE_SIZE, totalPages);

        Map<String, Integer> loginToPage = new HashMap<>();

        for (int pageNumber = 1; pageNumber <= totalPages; pageNumber++) {
            int fromIndex = (pageNumber - 1) * PAGE_SIZE;
            int toIndex = Math.min(fromIndex + PAGE_SIZE, totalPlayers);

            List<PlayerDto> playersOnPage = dtos.subList(fromIndex, toIndex);

            final int finalPageNumber = pageNumber; // to use in lambda, must be effectively final
            playersOnPage.forEach(player -> {
                String login = player.getLogin();
                if (StringUtils.isBlank(login)) {
                    logger.warn("Player {} has no login. Do not put it to login -> page map.", player.getPlayerId());
                    return;
                }

                loginToPage.put(login.toLowerCase(), finalPageNumber); // save lower-case login to make search case-insensitive
            });

            // export top by speed page to html
            String topBySpeedPageFilePath = PageUrls.getTopBySpeedPageFilePath(rootDir, pageNumber);

            new TopBySpeedPageTemplate()
                .totalRacesCountMin(TOTAL_RACES_COUNT_MIN)
                .totalPages(totalPages)
                .pageNumber(pageNumber)
                .players(playersOnPage)
                .export(topBySpeedPageFilePath);

            logger.debug("Exported page {}/{}.", pageNumber, totalPages);
        }

        // export login -> page map to a js file
        String loginToPageFilePath = PageUrls.getTopBySpeedLoginToPageFilePath(rootDir);

        String loginToPageString = JacksonUtils.serializeToString(loginToPage);

        new LoginToPageTemplate()
            .loginToPage(loginToPage)
            .loginToPageString(loginToPageString)
            .export(loginToPageFilePath);
    }

    private int getPagesCount(int totalRecords, int pageSize) {
        return ((totalRecords % pageSize) == 0) ? (totalRecords / pageSize) : ((totalRecords / pageSize) + 1);
    }
}
