package ru.klavogonki.statistics.freemarker;

import lombok.extern.log4j.Log4j2;
import ru.klavogonki.common.Rank;
import ru.klavogonki.statistics.Config;
import ru.klavogonki.statistics.entity.PlayerEntity;
import ru.klavogonki.statistics.entity.PlayerVocabularyStatsEntity;
import ru.klavogonki.statistics.export.ExportContext;
import ru.klavogonki.statistics.export.StatisticsGeneratorConfig;
import ru.klavogonki.statistics.export.vocabulary.NonStandardVocabularyGeneratorContext;
import ru.klavogonki.statistics.export.vocabulary.NonStandardVocabularyTopExporterGenerator;
import ru.klavogonki.statistics.repository.PlayerVocabularyStatsRepository;
import ru.klavogonki.statistics.util.JacksonUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * Used to try out the FreeMarker features without waiting for getting the data from the database.
 */
@Log4j2
public class ExampleTemplate extends FreemarkerTemplate {

    @Override
    public String getTemplatePath() {
        return "ftl/example.ftl";
    }

    @Override
    public void export(final ExportContext context, final String filePath) {
        templateData.put("testString", "A&nbsp;value&nbsp;test&nbsp;non&nbsp;breaking&nbsp;space!!!!");
        templateData.put("testInteger", 123456789);
        templateData.put("testDouble", 222.265);

        PlayerEntity player = new PlayerEntity();
        player.setLogin("nosferatum");
        player.setPlayerId(242585);
        player.setRankLevel(Rank.superman.level);

        templateData.put("testPlayer", player);

        Map<String, String> map = new TreeMap<>();
        map.put("key 1", "value 1");
        map.put("key 2", "value 2");
        map.put("key 3", "value 3");

        templateData.put("testMap", map);

        Map<Integer, PlayerEntity> idToPlayerMap = new TreeMap<>();
        idToPlayerMap.put(242585, player);
//        intMap.put(2, "value 2");

        templateData.put("idToPlayerMap", idToPlayerMap);


        String result = super.exportToString(context);
        logger.debug("result:\n{}", result);

        exportFreemarkerToFile(
            getTemplatePath(),
            filePath,
            templateData
        );
    }

    public static void main(String[] args) {
        // todo: extract this blood to ExportContextFactory
        String configFilePath = "C:/java/kgparser/kgstats-generated/2024.11.11/config-output.json";

        Config config = JacksonUtils.parseConfig(configFilePath);

        String generatorConfigFilePath = "C:/java/kgparser/kgstatsSrv/src/main/resources/statistics/statistics-generator-config-generate-all.json";
        StatisticsGeneratorConfig generatorConfig = JacksonUtils.parseStatisticsGeneratorConfig(generatorConfigFilePath);

        // no repository required for template
        PlayerVocabularyStatsRepository fakeRepository = createFakeRepository();

        // non-standard vocabularies exporters
//        val nonStandardDictionariesGeneratorContext =
        NonStandardVocabularyGeneratorContext nonStandardDictionariesGeneratorContext = NonStandardVocabularyTopExporterGenerator.INSTANCE.generateContext(
            generatorConfig.getNonStandardDictionariesCodes()
        );

        ExportContext context = new ExportContext(
            config,
            fakeRepository,
            nonStandardDictionariesGeneratorContext
        );

        // =================================================================
        String outputFileName = "C:\\java\\kgparser\\.ignoreme\\freemarker-example.html";

        new ExampleTemplate().export(context, outputFileName);
    }

    private static PlayerVocabularyStatsRepository createFakeRepository() {
        return new PlayerVocabularyStatsRepository() {
            @Override
            public List<PlayerVocabularyStatsEntity> findByVocabularyCodeEqualsAndRacesCountGreaterThanEqualAndPlayerBlockedEqualsOrderByBestSpeedDesc(String vocabularyCode, int racesCount, int blocked) {
                return null;
            }

            @Override
            public List<PlayerVocabularyStatsEntity> findByVocabularyCodeEqualsAndRacesCountGreaterThanEqualAndPlayerBlockedEqualsOrderByRacesCountDesc(String vocabularyCode, int racesCount, int blocked) {
                return null;
            }

            @Override
            public List<PlayerVocabularyStatsEntity> findByVocabularyCodeEqualsAndRacesCountGreaterThanEqualAndPlayerBlockedEqualsOrderByHaulDesc(String vocabularyCode, int racesCount, int blocked) {
                return null;
            }

            @Override
            public <S extends PlayerVocabularyStatsEntity> S save(S entity) {
                return null;
            }

            @Override
            public <S extends PlayerVocabularyStatsEntity> Iterable<S> saveAll(Iterable<S> entities) {
                return null;
            }

            @Override
            public Optional<PlayerVocabularyStatsEntity> findById(Long aLong) {
                return Optional.empty();
            }

            @Override
            public boolean existsById(Long aLong) {
                return false;
            }

            @Override
            public Iterable<PlayerVocabularyStatsEntity> findAll() {
                return null;
            }

            @Override
            public Iterable<PlayerVocabularyStatsEntity> findAllById(Iterable<Long> longs) {
                return null;
            }

            @Override
            public long count() {
                return 0;
            }

            @Override
            public void deleteById(Long aLong) {

            }

            @Override
            public void delete(PlayerVocabularyStatsEntity entity) {

            }

            @Override
            public void deleteAllById(Iterable<? extends Long> longs) {

            }

            @Override
            public void deleteAll(Iterable<? extends PlayerVocabularyStatsEntity> entities) {

            }

            @Override
            public void deleteAll() {

            }
        };
    }
}
