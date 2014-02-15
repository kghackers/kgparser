package ru.klavogonki.kgparser.servlet.model.basicInfo;

import ru.klavogonki.kgparser.Competition;
import ru.klavogonki.kgparser.Dictionary;
import ru.klavogonki.kgparser.Player;
import ru.klavogonki.kgparser.Round;
import su.opencode.kefir.srv.json.JsonObject;
import su.opencode.kefir.util.DateUtils;

import java.text.DateFormat;
import java.util.*;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public class CompetitionBasicInfo extends JsonObject
{
	public CompetitionBasicInfo() {
	}
	public CompetitionBasicInfo(Competition competition) {
		this.name = competition.getName();
		this.roundsCount = competition.getRounds().size();

		this.dictionaries = new ArrayList<>();
		Set<Dictionary> competitionDictionaries = competition.getDictionaries();
		for (Dictionary dictionary : competitionDictionaries)
		{
			CompetitionDictionary vo = new CompetitionDictionary();
			vo.setDictionaryName(dictionary.getName());
			vo.setDictionaryIsStandard(dictionary.isStandard());
			vo.setDictionaryLink(dictionary.getDictionaryPageUrl());
			vo.setDictionaryColor(dictionary.getColor());
			vo.setRoundsCount( competition.getRoundsCount(dictionary) );

			this.dictionaries.add(vo);
		}

		this.players = new ArrayList<>();
		Set<Player> competitionPlayers = competition.getPlayers();
		// todo: sortPlayers somehow
		for (Player player : competitionPlayers)
		{
			PlayerBasicInfo vo = new PlayerBasicInfo();
			vo.setProfileId(player.getProfileId());
			vo.setName(player.getName());
			vo.setRank(player.getRank().toString());
			vo.setRankColor(player.getColor());
			vo.setTotalRoundsCount(competition.getRoundsCount(player));

			Map<String, Integer> dictionariesRoundCount = new HashMap<>();
			for (Dictionary dictionary : competitionDictionaries)
			{
				int dictionaryRoundsCount = competition.getRoundsCount(player, dictionary);
				dictionariesRoundCount.put(dictionary.getCode(), dictionaryRoundsCount);
			}
			vo.setDictionariesRoundsCount(dictionariesRoundCount);

			this.players.add(vo);
		}

		DateFormat dateFormat = DateUtils.getDayMonthYearHourMinuteSecondFormat();
		this.rounds = new ArrayList<>();
		List<Round> competitionRounds = competition.getRounds();
		for (Round competitionRound : competitionRounds)
		{
			RoundBasicInfo vo = new RoundBasicInfo();
			vo.setNumber( competitionRound.getNumber() );
			vo.setNumberInDictionary(competitionRound.getNumberInDictionary());

			Dictionary dictionary = competitionRound.getDictionary();
			vo.setDictionaryName(dictionary.getName());
			vo.setDictionaryIsStandard(dictionary.isStandard());
			vo.setDictionaryLink(dictionary.getDictionaryPageUrl());
			vo.setDictionaryColor(dictionary.getColor());

			vo.setBeginTime(competitionRound.getBeginTime());
			vo.setBeginTimeStr(dateFormat.format(competitionRound.getBeginTime()));

			vo.setFinishedPlayersCount( competitionRound.getResults().size() );

			this.rounds.add(vo);
		}
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getRoundsCount() {
		return roundsCount;
	}
	public void setRoundsCount(Integer roundsCount) {
		this.roundsCount = roundsCount;
	}
	public List<CompetitionDictionary> getDictionaries() {
		return dictionaries;
	}
	public void setDictionaries(List<CompetitionDictionary> dictionaries) {
		this.dictionaries = dictionaries;
	}
	public List<PlayerBasicInfo> getPlayers() {
		return players;
	}
	public void setPlayers(List<PlayerBasicInfo> players) {
		this.players = players;
	}
	public List<RoundBasicInfo> getRounds() {
		return rounds;
	}
	public void setRounds(List<RoundBasicInfo> rounds) {
		this.rounds = rounds;
	}

	private String name;

	private Integer roundsCount;

	private List<CompetitionDictionary> dictionaries;

	private List<PlayerBasicInfo> players;

	private List<RoundBasicInfo> rounds;
}