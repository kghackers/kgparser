package ru.klavogonki.kgparser.springboot;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.klavogonki.kgparser.jsonParser.JacksonUtils;
import ru.klavogonki.kgparser.jsonParser.dto.PlayerDto;
import ru.klavogonki.kgparser.jsonParser.entity.PlayerEntity;
import ru.klavogonki.kgparser.jsonParser.mapper.PlayerDtoMapper;
import ru.klavogonki.kgparser.jsonParser.repository.PlayerRepository;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Component
public class TopBySpeedExporter {
    private static final int TOTAL_RACES_COUNT_MIN = 1000;
    private static final int PAGE_SIZE = 100;

    private static final String FTL_TEMPLATE = "top-by-speed-page.ftl";
    private static final String FTL_TOTAL_PAGES_KEY = "totalPages";
    private static final String FTL_PAGE_NUMBER_KEY = "pageNumber";
    private static final String FTL_PLAYERS_KEY = "players";
    private static final String FTL_TOTAL_RACES_COUNT_MIN_KEY = "totalRacesCountMin";

    private static final String FTL_LOGIN_TO_PAGE_TEMPLATE = "top-by-speed-login-to-page.ftl";
    private static final String FTL_LOGIN_TO_PAGE_KEY = "loginToPage";

    private static final String FREEMARKER_VERSION = "2.3.30";

    @Autowired
    private PlayerRepository playerRepository;

    // todo: autowire it, @see https://mapstruct.org/documentation/stable/reference/html/#using-dependency-injection
    private final PlayerDtoMapper mapper = Mappers.getMapper(PlayerDtoMapper.class);

    public void export() {
        List<PlayerEntity> players = playerRepository.findByTotalRacesCountGreaterThanEqualAndBlockedEqualsOrderByBestSpeedDescTotalRacesCountDesc(
            TOTAL_RACES_COUNT_MIN,
            0
        );

        List<PlayerDto> dtos = mapper.playerEntitiesToPlayerDtos(players);

        int totalPlayers = players.size();
        logger.debug("Total players: {}", totalPlayers);

        // todo: get rootDir from config
        String rootDir = "C:/java/kgparser/kgparserWeb/src/main/webapp/";

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

            String fileName = getFileName(pageNumber);
            String filePath = rootDir + fileName;

            exportTopPageToHtml(totalPages, pageNumber, playersOnPage, filePath);
            logger.debug("Exported page {}/{}.", pageNumber, totalPages);
        }

        // export login -> page map to a js file
        String loginToPageFilePath = rootDir + "/js/stat-top-by-best-speed-login-to-page.js";

        String loginToPageString = JacksonUtils.serializeToString(loginToPage);
        exportLoginToPageToJs(loginToPageFilePath, loginToPageString, loginToPage);
    }

    private String getFileName(final int pageNumber) {
        return "stat-top-by-best-speed-page-" + pageNumber + ".html";
    }

    private int getPagesCount(int totalRecords, int pageSize) {
        return ((totalRecords % pageSize) == 0) ? (totalRecords / pageSize) : ((totalRecords / pageSize) + 1);
    }

    public static void exportTopPageToHtml(int totalPages, int pageNumber, List<PlayerDto> players, String filePath) {
        Map<String, Object> templateData = new HashMap<>();
        templateData.put(FTL_TOTAL_PAGES_KEY, totalPages);
        templateData.put(FTL_PAGE_NUMBER_KEY, pageNumber);
        templateData.put(FTL_PLAYERS_KEY, players);
        templateData.put(FTL_TOTAL_RACES_COUNT_MIN_KEY, Integer.toString(TOTAL_RACES_COUNT_MIN)); // to avoid ugly Freemarker formatting

        exportFreemarker(FTL_TEMPLATE, filePath, templateData);
        logger.debug("Top speed {} players (page {}) exported to file {}", players.size(), pageNumber, filePath);
    }

    public static void exportLoginToPageToJs(String filePath, String loginToPageString, Map<String, Integer> loginToPage) {
        Map<String, Object> templateData = new HashMap<>();
        templateData.put(FTL_LOGIN_TO_PAGE_KEY, loginToPageString);

        exportFreemarker(FTL_LOGIN_TO_PAGE_TEMPLATE, filePath, templateData);
        logger.debug("Saved login -> page mapping to file {}. Total logins: {}", filePath, loginToPage.size());
    }

    // todo: move to some FreemarkerUtils class
    private static void exportFreemarker(final String ftlTemplate, final String filePath, final Map<String, Object> templateData) {
        try (FileWriter out = new FileWriter(filePath)) {
            Configuration cfg = new Configuration(new Version(FREEMARKER_VERSION));

            cfg.setClassForTemplateLoading(TopBySpeedExporter.class, "/");
            cfg.setDefaultEncoding(StandardCharsets.UTF_8.displayName());

            Template template = cfg.getTemplate(ftlTemplate);

            template.process(templateData, out);

            out.flush();
        }
        catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }
}
