package ru.klavogonki.statistics.export;

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

    public void generateStatistics(final Config config) {
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

        // todo: select mode (what to do) by arguments
        // todo: add an option to skip Excel import

        boolean exitAfterPageGeneration = true; // TODO: DO NOT COMMIT THIS!

        // non-standard vocabularies exporters
        pinkiesPlusTopExporter.export(context);
        if (exitAfterPageGeneration) {
            return;
        }

        ringFingersTopExporter.export(context);
        if (exitAfterPageGeneration) {
            return;
        }

        trainingIndexFingersTopExporter.export(context);
        if (exitAfterPageGeneration) {
            return;
        }

        digitsOneHundredTopExporter.export(context);
        if (exitAfterPageGeneration) {
            return;
        }

        oneHundredRussianTopExporter.export(context);
        if (exitAfterPageGeneration) {
            return;
        }

        shortTextsTopExporter.export(context);
        if (exitAfterPageGeneration) {
            return;
        }

        frequencyVocabularyTopExporter.export(context);
        if (exitAfterPageGeneration) {
            return;
        }

        miniMarathonTopExporter.export(context);
        if (exitAfterPageGeneration) {
            return;
        }

        normalInEnglishTopExporter.export(context);
        if (exitAfterPageGeneration) {
            return;
        }

        // standard vocabularies exporters
        sprintTopExporter.export(context);
        if (exitAfterPageGeneration) {
            return;
        }

        digitsTopExporter.export(context);
        if (exitAfterPageGeneration) {
            return;
        }

        marathonTopExporter.export(context);
        if (exitAfterPageGeneration) {
            return;
        }

        referatsTopExporter.export(context);
        if (exitAfterPageGeneration) {
            return;
        }

        abraTopExporter.export(context);
        if (exitAfterPageGeneration) {
            return;
        }

        normalTopExporter.export(context);
        if (exitAfterPageGeneration) {
            return;
        }

        noErrorTopExporter.export(context);
        if (exitAfterPageGeneration) {
            return;
        }

        charsTopExporter.export(context);
        if (exitAfterPageGeneration) {
            return;
        }

        // global tops exporters
        playersByRankExporter.export(context);
        if (exitAfterPageGeneration) {
            return;
        }

        top500PagesExporter.export(context);
        if (exitAfterPageGeneration) {
            return;
        }

        indexPageExporter.export(context);
        if (exitAfterPageGeneration) {
            return;
        }

        topBySpeedExporter.export(context);
        if (exitAfterPageGeneration) {
            return;
        }
    }
}
