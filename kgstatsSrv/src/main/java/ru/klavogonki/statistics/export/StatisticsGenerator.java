package ru.klavogonki.statistics.export;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.klavogonki.statistics.Config;
import ru.klavogonki.statistics.export.vocabulary.non_standard.DigitsOneHundredTopExporter;
import ru.klavogonki.statistics.export.vocabulary.non_standard.FrequencyVocabularyTopExporter;
import ru.klavogonki.statistics.export.vocabulary.non_standard.MiniMarathonTopExporter;
import ru.klavogonki.statistics.export.vocabulary.non_standard.NormalInEnglishTopExporter;
import ru.klavogonki.statistics.export.vocabulary.non_standard.OneHundredRussianTopExporter;
import ru.klavogonki.statistics.export.vocabulary.non_standard.PinkiesPlusTopExporter;
import ru.klavogonki.statistics.export.vocabulary.non_standard.RingFingersTopExporter;
import ru.klavogonki.statistics.export.vocabulary.non_standard.ShortTextsTopExporter;
import ru.klavogonki.statistics.export.vocabulary.non_standard.TrainingIndexFingersTopExporter;
import ru.klavogonki.statistics.export.vocabulary.standard.AbraTopExporter;
import ru.klavogonki.statistics.export.vocabulary.standard.CharsTopExporter;
import ru.klavogonki.statistics.export.vocabulary.standard.DigitsTopExporter;
import ru.klavogonki.statistics.export.vocabulary.standard.MarathonTopExporter;
import ru.klavogonki.statistics.export.vocabulary.standard.NoErrorTopExporter;
import ru.klavogonki.statistics.export.vocabulary.standard.NormalTopExporter;
import ru.klavogonki.statistics.export.vocabulary.standard.ReferatsTopExporter;
import ru.klavogonki.statistics.export.vocabulary.standard.SprintTopExporter;
import ru.klavogonki.statistics.springboot.Profiles;

@Component
@Profile(Profiles.DATABASE)
@Log4j2
public class StatisticsGenerator {

    // aggregate tops
    @Autowired
    private IndexPageExporter indexPageExporter;

    @Autowired
    private TopBySpeedExporter topBySpeedExporter;

    @Autowired
    private Top500PagesExporter top500PagesExporter;

    @Autowired
    private PlayersByRankExporter playersByRankExporter;

    // standard vocabularies
    @Autowired
    private NormalTopExporter normalTopExporter;

    @Autowired
    private AbraTopExporter abraTopExporter;

    @Autowired
    private ReferatsTopExporter referatsTopExporter;

    @Autowired
    private NoErrorTopExporter noErrorTopExporter;

    @Autowired
    private MarathonTopExporter marathonTopExporter;

    @Autowired
    private CharsTopExporter charsTopExporter;

    @Autowired
    private DigitsTopExporter digitsTopExporter;

    @Autowired
    private SprintTopExporter sprintTopExporter;

    // non-standard vocabularies
    @Autowired
    private NormalInEnglishTopExporter normalInEnglishTopExporter;

    @Autowired
    private MiniMarathonTopExporter miniMarathonTopExporter;

    @Autowired
    private ShortTextsTopExporter shortTextsTopExporter;

    @Autowired
    private FrequencyVocabularyTopExporter frequencyVocabularyTopExporter;

    @Autowired
    private OneHundredRussianTopExporter oneHundredRussianTopExporter;

    @Autowired
    private DigitsOneHundredTopExporter digitsOneHundredTopExporter;

    @Autowired
    private TrainingIndexFingersTopExporter trainingIndexFingersTopExporter;

    @Autowired
    private RingFingersTopExporter ringFingersTopExporter;

    @Autowired
    private PinkiesPlusTopExporter pinkiesPlusTopExporter;

    public void generateStatistics(final Config config, final StatisticsGeneratorConfig generatorConfig) {
        ExportContext context = new ExportContext(config);
/*
		context.webRootDir = "C:/java/kgparser/kgstats/src/main/webapp/";

		// data load from 2020-12-28
		context.minPlayerId = 1;
		context.maxPlayerId = 628000;
		context.dataDownloadStartDate = DateUtils.parseLocalDateTimeWithUiDateFormat("2020-12-28 00:28:13");
		context.dataDownloadEndDate = DateUtils.parseLocalDateTimeWithUiDateFormat("2020-12-28 01:44:43");
*/

/*
		// data load from 2020-12-09
		context.minPlayerId = 1;
		context.maxPlayerId = 625000;
		context.dataDownloadStartDate = DateUtils.parseLocalDateTimeWithUiDateFormat("2020-12-08 02:39:07");
		context.dataDownloadEndDate = DateUtils.parseLocalDateTimeWithUiDateFormat("2020-12-09 16:28:01");
*/

        // todo: add an option to skip Excel import

        logger.debug("{}: {}", StatisticsGeneratorConfig.class.getSimpleName(), generatorConfig);

        // global tops exporters
        export(context, generatorConfig.getExportIndexPage(), indexPageExporter);
        export(context, generatorConfig.getExportTopBySpeed(), topBySpeedExporter);
        export(context, generatorConfig.getExportTop500Pages(), top500PagesExporter);
        export(context, generatorConfig.getExportPlayersByRank(), playersByRankExporter);

        // standard vocabularies exporters
        export(context, generatorConfig.getExportNormalTop(), normalTopExporter);
        export(context, generatorConfig.getExportAbraTop(), abraTopExporter);
        export(context, generatorConfig.getExportReferatsTop(), referatsTopExporter);
        export(context, generatorConfig.getExportNoErrorTop(), noErrorTopExporter);
        export(context, generatorConfig.getExportMarathonTop(), marathonTopExporter);
        export(context, generatorConfig.getExportCharsTop(), charsTopExporter);
        export(context, generatorConfig.getExportDigitsTop(), digitsTopExporter);
        export(context, generatorConfig.getExportSprintTop(), sprintTopExporter);

        // non-standard vocabularies exporters
        export(context, generatorConfig.getExportNormalInEnglishTop(), normalInEnglishTopExporter);
        export(context, generatorConfig.getExportMiniMarathonTop(), miniMarathonTopExporter);
        export(context, generatorConfig.getExportShortTextsTop(), shortTextsTopExporter);
        export(context, generatorConfig.getExportFrequencyVocabularyTop(), frequencyVocabularyTopExporter);
        export(context, generatorConfig.getExportOneHundredRussianTop(), oneHundredRussianTopExporter);
        export(context, generatorConfig.getExportDigitsOneHundredTop(), digitsOneHundredTopExporter);
        export(context, generatorConfig.getExportTrainingIndexFingersTop(), trainingIndexFingersTopExporter);
        export(context, generatorConfig.getExportRingFingersTop(), ringFingersTopExporter);
        export(context, generatorConfig.getExportPinkiesPlusTop(), pinkiesPlusTopExporter);
    }

    // todo: think about moving StatisticsGeneratorConfig field determination to Exporter interface
    private void export(ExportContext context, boolean export, DataExporter exporter) {
        logger.info("===============================================");

        if (!export) {
            logger.info(
                "{} flag for {} is {}. Do not execute the export.",
                StatisticsGeneratorConfig.class.getSimpleName(),
                exporter.getClass().getSimpleName(),
                false
            );

            return;
        }

        logger.info(
            "{} flag for {} is {}. Execute the export.",
            StatisticsGeneratorConfig.class.getSimpleName(),
            exporter.getClass().getSimpleName(),
            true
        );

        exporter.export(context);
    }
}
