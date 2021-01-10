package ru.klavogonki.kgparser.export;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.klavogonki.kgparser.excel.TopByBestSpeedPageExcelTemplate;
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
public class TopBySpeedExporter implements DataExporter {
    private static final int TOTAL_RACES_COUNT_MIN = 1000;
    private static final int PAGE_SIZE = 100;

    @Autowired
    private PlayerRepository playerRepository;

    // todo: autowire it, @see https://mapstruct.org/documentation/stable/reference/html/#using-dependency-injection
    private final PlayerDtoMapper mapper = Mappers.getMapper(PlayerDtoMapper.class);

    public void export(ExportContext context) {
        // !!! todo: !user paged query instead of manually splitting the result to pages
        // todo: this will require changes to order number fill: ability to pass the start number. Therefore too complex (since same value can split over multiple pages)
        List<PlayerEntity> players = playerRepository.findByTotalRacesCountGreaterThanEqualAndBlockedEqualsOrderByBestSpeedDescTotalRacesCountDesc(
            TOTAL_RACES_COUNT_MIN,
            PlayerEntity.NOT_BLOCKED
        );

        List<PlayerDto> dtos = mapper.playerEntitiesToPlayerDtos(players, PlayerDto::getBestSpeed);

        int totalPlayers = players.size();
        logger.debug("Total players: {}", totalPlayers);

        int totalPages = ExporterUtils.getPagesCount(totalPlayers, PAGE_SIZE);
        logger.debug("Total records: {}. Page size: {}. Total pages: {}", totalPlayers, PAGE_SIZE, totalPages);

        Map<String, Integer> loginToPage = new HashMap<>();

        for (int pageNumber = ExporterUtils.FIRST_PAGE_NUMBER; pageNumber <= totalPages; pageNumber++) {
            List<PlayerDto> playersOnPage = ExporterUtils.subList(dtos, PAGE_SIZE, pageNumber);

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
            String topBySpeedPageFilePath = PageUrls.getTopBySpeedPageFilePath(context.webRootDir, pageNumber);

            new TopBySpeedPageTemplate()
                .totalRacesCountMin(TOTAL_RACES_COUNT_MIN)
                .totalPages(totalPages)
                .pageNumber(pageNumber)
                .players(playersOnPage)
                .export(topBySpeedPageFilePath);

            logger.debug("Exported page {}/{}.", pageNumber, totalPages);
        }

        exportLoginToPageMapToJs(context, loginToPage);

        exportAllPagesToExcel(context, dtos);
    }

    public void exportLoginToPageMapToJs(final ExportContext context, final Map<String, Integer> loginToPage) {
        // export login -> page map to a js file
        String loginToPageFilePath = PageUrls.getTopBySpeedLoginToPageFilePath(context.webRootDir);

        String loginToPageString = JacksonUtils.serializeToString(loginToPage);

        new LoginToPageTemplate()
            .loginToPage(loginToPage)
            .loginToPageString(loginToPageString)
            .export(loginToPageFilePath);
    }

    public void exportAllPagesToExcel(final ExportContext context, final List<PlayerDto> dtos) {
        // export all pages to one Excel
        new TopByBestSpeedPageExcelTemplate()
            .players(dtos)
            .export(
                PageUrls.getTopBySpeedAllPagesExcelFilePath(context.webRootDir),
                PageUrls.getTopBySpeedAllPagesExcelZipFilePath(context.webRootDir)
            );
    }
}
