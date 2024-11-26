package ru.klavogonki.statistics.export

import lombok.extern.log4j.Log4j2
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import ru.klavogonki.statistics.Config
import ru.klavogonki.statistics.export.vocabulary.non_standard.DigitsOneHundredTopExporter
import ru.klavogonki.statistics.export.vocabulary.non_standard.FrequencyVocabularyTopExporter
import ru.klavogonki.statistics.export.vocabulary.non_standard.MiniMarathonTopExporter
import ru.klavogonki.statistics.export.vocabulary.non_standard.NormalInEnglishTopExporter
import ru.klavogonki.statistics.export.vocabulary.non_standard.OneHundredRussianTopExporter
import ru.klavogonki.statistics.export.vocabulary.non_standard.PinkiesPlusTopExporter
import ru.klavogonki.statistics.export.vocabulary.non_standard.RingFingersTopExporter
import ru.klavogonki.statistics.export.vocabulary.non_standard.ShortTextsTopExporter
import ru.klavogonki.statistics.export.vocabulary.non_standard.TrainingIndexFingersTopExporter
import ru.klavogonki.statistics.export.vocabulary.standard.AbraTopExporter
import ru.klavogonki.statistics.export.vocabulary.standard.CharsTopExporter
import ru.klavogonki.statistics.export.vocabulary.standard.DigitsTopExporter
import ru.klavogonki.statistics.export.vocabulary.standard.MarathonTopExporter
import ru.klavogonki.statistics.export.vocabulary.standard.NoErrorTopExporter
import ru.klavogonki.statistics.export.vocabulary.standard.NormalTopExporter
import ru.klavogonki.statistics.export.vocabulary.standard.ReferatsTopExporter
import ru.klavogonki.statistics.export.vocabulary.standard.SprintTopExporter
import ru.klavogonki.statistics.springboot.Profiles

@Component
@Profile(Profiles.DATABASE)
@Log4j2
class StatisticsGenerator : Logging {
    // aggregate tops
    @Autowired
    private val indexPageExporter: IndexPageExporter? = null

    @Autowired
    private val topBySpeedExporter: TopBySpeedExporter? = null

    @Autowired
    private val top500PagesExporter: Top500PagesExporter? = null

    @Autowired
    private val playersByRankExporter: PlayersByRankExporter? = null

    // standard vocabularies
    @Autowired
    private val normalTopExporter: NormalTopExporter? = null

    @Autowired
    private val abraTopExporter: AbraTopExporter? = null

    @Autowired
    private val referatsTopExporter: ReferatsTopExporter? = null

    @Autowired
    private val noErrorTopExporter: NoErrorTopExporter? = null

    @Autowired
    private val marathonTopExporter: MarathonTopExporter? = null

    @Autowired
    private val charsTopExporter: CharsTopExporter? = null

    @Autowired
    private val digitsTopExporter: DigitsTopExporter? = null

    @Autowired
    private val sprintTopExporter: SprintTopExporter? = null

    // non-standard vocabularies
    @Autowired
    private val normalInEnglishTopExporter: NormalInEnglishTopExporter? = null

    @Autowired
    private val miniMarathonTopExporter: MiniMarathonTopExporter? = null

    @Autowired
    private val shortTextsTopExporter: ShortTextsTopExporter? = null

    @Autowired
    private val frequencyVocabularyTopExporter: FrequencyVocabularyTopExporter? = null

    @Autowired
    private val oneHundredRussianTopExporter: OneHundredRussianTopExporter? = null

    @Autowired
    private val digitsOneHundredTopExporter: DigitsOneHundredTopExporter? = null

    @Autowired
    private val trainingIndexFingersTopExporter: TrainingIndexFingersTopExporter? = null

    @Autowired
    private val ringFingersTopExporter: RingFingersTopExporter? = null

    @Autowired
    private val pinkiesPlusTopExporter: PinkiesPlusTopExporter? = null

    fun generateStatistics(config: Config, generatorConfig: StatisticsGeneratorConfig) {
        val context = ExportContext(config)

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
        logger.debug("${StatisticsGeneratorConfig::class.java.simpleName}: $generatorConfig")

        // global tops exporters
        export(context, generatorConfig.exportIndexPage, indexPageExporter)
        export(context, generatorConfig.exportTopBySpeed, topBySpeedExporter)
        export(context, generatorConfig.exportTop500Pages, top500PagesExporter)
        export(context, generatorConfig.exportPlayersByRank, playersByRankExporter)

        // standard vocabularies exporters
        export(context, generatorConfig.exportNormalTop, normalTopExporter)
        export(context, generatorConfig.exportAbraTop, abraTopExporter)
        export(context, generatorConfig.exportReferatsTop, referatsTopExporter)
        export(context, generatorConfig.exportNoErrorTop, noErrorTopExporter)
        export(context, generatorConfig.exportMarathonTop, marathonTopExporter)
        export(context, generatorConfig.exportCharsTop, charsTopExporter)
        export(context, generatorConfig.exportDigitsTop, digitsTopExporter)
        export(context, generatorConfig.exportSprintTop, sprintTopExporter)

        // non-standard vocabularies exporters
        export(context, generatorConfig.exportNormalInEnglishTop, normalInEnglishTopExporter)
        export(context, generatorConfig.exportMiniMarathonTop, miniMarathonTopExporter)
        export(context, generatorConfig.exportShortTextsTop, shortTextsTopExporter)
        export(context, generatorConfig.exportFrequencyVocabularyTop, frequencyVocabularyTopExporter)
        export(context, generatorConfig.exportOneHundredRussianTop, oneHundredRussianTopExporter)
        export(context, generatorConfig.exportDigitsOneHundredTop, digitsOneHundredTopExporter)
        export(context, generatorConfig.exportTrainingIndexFingersTop, trainingIndexFingersTopExporter)
        export(context, generatorConfig.exportRingFingersTop, ringFingersTopExporter)
        export(context, generatorConfig.exportPinkiesPlusTop, pinkiesPlusTopExporter)
    }

    // todo: think about moving StatisticsGeneratorConfig field determination to Exporter interface
    private fun export(
        context: ExportContext,
        export: Boolean,
        exporter: DataExporter?
    ) {
        checkNotNull(exporter)

        logger.info("===============================================")

        if (!export) {
            logger.info(
                "${StatisticsGeneratorConfig::class.java.simpleName} flag" +
                    " for ${exporter.javaClass.simpleName} is ${false}. Do not execute the export.",
            )

            return
        }

        logger.info(
            "${StatisticsGeneratorConfig::class.java.simpleName} flag" +
                " for ${exporter.javaClass.simpleName} is ${true}. Execute the export.",
        )

        exporter.export(context)
    }
}
