package ru.klavogonki.statistics.export

import lombok.extern.log4j.Log4j2
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import ru.klavogonki.common.NonStandardDictionary
import ru.klavogonki.statistics.Config
import ru.klavogonki.statistics.export.vocabulary.NonStandardVocabularyTopExporterGenerator
import ru.klavogonki.statistics.export.vocabulary.StandardVocabularyTopExporterGenerator
import ru.klavogonki.statistics.export.vocabulary.VocabularyTopExporter
import ru.klavogonki.statistics.export.vocabulary.VocabularyTopUtils
import ru.klavogonki.statistics.export.vocabulary.non_standard.NonStandardVocabularyTopExporter
import ru.klavogonki.statistics.freemarker.Links
import ru.klavogonki.statistics.repository.PlayerVocabularyStatsRepository
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

    @Autowired
    private val repository: PlayerVocabularyStatsRepository? = null

    fun generateStatistics(config: Config, generatorConfig: StatisticsGeneratorConfig) {
        checkNotNull(repository) {
            "PlayerVocabularyStatsRepository is null. Check your Spring profiles."
        }

        // standard vocabularies exporters
        val standardDictionariesGeneratorContext =
            StandardVocabularyTopExporterGenerator.generateContext(generatorConfig.standardDictionariesCodes)

        // non-standard vocabularies exporters
        val nonStandardDictionariesGeneratorContext =
            NonStandardVocabularyTopExporterGenerator.generateContext(generatorConfig.nonStandardDictionariesCodes)

        val links = Links.create(
            standardDictionariesGeneratorContext,
            nonStandardDictionariesGeneratorContext
        )

        // does not include the "special" exporters for non-vocabularies
        val allTopExporters: MutableList<VocabularyTopExporter> = mutableListOf()
        allTopExporters.addAll(standardDictionariesGeneratorContext.getAllExporters())
        allTopExporters.addAll(nonStandardDictionariesGeneratorContext.getAllExporters())

        // validate Excel sheet names
        VocabularyTopUtils.validateTopExporters(allTopExporters)

        // create ExportContext
        val context = ExportContext(
            config,
            repository,
            standardDictionariesGeneratorContext,
            nonStandardDictionariesGeneratorContext,
            links
        )

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
        generatorConfig.standardDictionariesCodes.forEach {
            val exporter = standardDictionariesGeneratorContext.getExporter(it)

            // export flag is true since we're only iterating the dictionaries set in the config
            export(context, true, exporter)
        }

        // non-standard vocabularies exporters
        generatorConfig.nonStandardDictionariesCodes.forEach {
            val exporter = nonStandardDictionariesGeneratorContext.getExporter(it)

            // export flag is true since we're only iterating the dictionaries set in the config
            export(context, true, exporter)
        }
    }

    // todo: think about moving StatisticsGeneratorConfig field determination to Exporter interface
    private fun export(
        context: ExportContext,
        generatorConfig: StatisticsGeneratorConfig,
        nonStandardDictionary: NonStandardDictionary,
        exporter: DataExporter?
    ) {
        export(
            context,
            generatorConfig,
            nonStandardDictionary.id,
            exporter
        )
    }

    private fun export(
        context: ExportContext,
        generatorConfig: StatisticsGeneratorConfig,
        nonStandardDictionaryId: Int,
        exporter: DataExporter?
    ) {
        val export = generatorConfig.nonStandardDictionariesCodes.contains(nonStandardDictionaryId)

        export(
            context,
            export,
            exporter
        )
    }

    private fun export(
        context: ExportContext,
        export: Boolean,
        exporter: DataExporter?
    ) {
        checkNotNull(exporter)

        logger.info("===============================================")

        val exporterName = getExporterName(exporter)

        if (!export) {
            logger.info(
                "${StatisticsGeneratorConfig::class.java.simpleName} flag" +
                    " for $exporterName is ${false}. Do not execute the export.",
            )

            return
        }

        logger.info(
            "${StatisticsGeneratorConfig::class.java.simpleName} flag" +
                " for $exporterName is ${true}. Execute the export.",
        )

        exporter.export(context)
    }

    private fun getExporterName(exporter: DataExporter): String {
        // todo: we may just return exporter name from DataExporter interface

        if (exporter is NonStandardVocabularyTopExporter) {
            return exporter.vocabularyTopData().loggerName
        }

        // other exporters now have
        // todo: if we switch completely to generated exporters -> user
        return exporter.javaClass.simpleName
    }
}