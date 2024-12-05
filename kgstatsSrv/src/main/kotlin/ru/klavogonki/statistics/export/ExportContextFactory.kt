package ru.klavogonki.statistics.export

import ru.klavogonki.statistics.entity.PlayerVocabularyStatsEntity
import ru.klavogonki.statistics.export.vocabulary.NonStandardVocabularyTopExporterGenerator.generateContext
import ru.klavogonki.statistics.freemarker.Links
import ru.klavogonki.statistics.repository.PlayerVocabularyStatsRepository
import ru.klavogonki.statistics.util.JacksonUtils
import java.util.Optional

object ExportContextFactory {

    fun createMock() = createMock(
        "C:/java/kgparser/kgstats-generated/2024.11.11/config-output.json",
        "C:/java/kgparser/kgstatsSrv/src/main/resources/statistics/statistics-generator-config-generate-all.json"
    )

    fun createMock(
        configFilePath: String,
        generatorConfigFilePath: String
    ): ExportContext {
        val config = JacksonUtils.parseConfig(configFilePath)
        val generatorConfig = JacksonUtils.parseStatisticsGeneratorConfig(generatorConfigFilePath)

        // non-standard vocabularies exporters
        val nonStandardDictionariesGeneratorContext = generateContext(
            generatorConfig.nonStandardDictionariesCodes
        )

        val links = Links.create(nonStandardDictionariesGeneratorContext)

        // no repository required for freemarker templates
        val fakeRepository = createFakeRepository()

        return ExportContext(
            config,
            fakeRepository,
            nonStandardDictionariesGeneratorContext,
            links
        )
    }

    private fun createFakeRepository(): PlayerVocabularyStatsRepository {

        return object : PlayerVocabularyStatsRepository {
            override fun <S : PlayerVocabularyStatsEntity?> save(entity: S): S {
                TODO("Not yet implemented")
            }

            override fun <S : PlayerVocabularyStatsEntity?> saveAll(entities: MutableIterable<S>): MutableIterable<S> {
                TODO("Not yet implemented")
            }

            override fun findById(id: Long): Optional<PlayerVocabularyStatsEntity> {
                TODO("Not yet implemented")
            }

            override fun existsById(id: Long): Boolean {
                TODO("Not yet implemented")
            }

            override fun findAll(): MutableIterable<PlayerVocabularyStatsEntity> {
                TODO("Not yet implemented")
            }

            override fun findAllById(ids: MutableIterable<Long>): MutableIterable<PlayerVocabularyStatsEntity> {
                TODO("Not yet implemented")
            }

            override fun count(): Long {
                TODO("Not yet implemented")
            }

            override fun deleteById(id: Long) {
                TODO("Not yet implemented")
            }

            override fun delete(entity: PlayerVocabularyStatsEntity) {
                TODO("Not yet implemented")
            }

            override fun deleteAllById(ids: MutableIterable<Long>) {
                TODO("Not yet implemented")
            }

            override fun deleteAll(entities: MutableIterable<PlayerVocabularyStatsEntity>) {
                TODO("Not yet implemented")
            }

            override fun deleteAll() {
                TODO("Not yet implemented")
            }

            override fun findByVocabularyCodeEqualsAndRacesCountGreaterThanEqualAndPlayerBlockedEqualsOrderByBestSpeedDesc(
                vocabularyCode: String?,
                racesCount: Int,
                blocked: Int
            ): MutableList<PlayerVocabularyStatsEntity> {
                TODO("Not yet implemented")
            }

            override fun findByVocabularyCodeEqualsAndRacesCountGreaterThanEqualAndPlayerBlockedEqualsOrderByRacesCountDesc(
                vocabularyCode: String?,
                racesCount: Int,
                blocked: Int
            ): MutableList<PlayerVocabularyStatsEntity> {
                TODO("Not yet implemented")
            }

            override fun findByVocabularyCodeEqualsAndRacesCountGreaterThanEqualAndPlayerBlockedEqualsOrderByHaulDesc(
                vocabularyCode: String?,
                racesCount: Int,
                blocked: Int
            ): MutableList<PlayerVocabularyStatsEntity> {
                TODO("Not yet implemented")
            }
        }
    }
}