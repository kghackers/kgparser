package ru.klavogonki.kgparser.servlet.model.round;

import ru.klavogonki.kgparser.Competition;
import ru.klavogonki.kgparser.PlayerRoundResult;
import ru.klavogonki.kgparser.PlayerRoundResultPlacesComparator;
import ru.klavogonki.kgparser.Round;
import ru.klavogonki.kgparser.servlet.model.basic_info.CompetitionDictionary;
import su.opencode.kefir.srv.json.JsonObject;
import su.opencode.kefir.util.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Объект, хранящий данные для отображения на странице заезда.
 */
public class RoundInfo extends JsonObject
{
	public RoundInfo() {
	}

	public RoundInfo(Competition competition, Round round) {
		this.competitionName = competition.getName();

		this.dictionary = new CompetitionDictionary( round.getDictionary() );

		this.number = round.getNumber();
		this.numberInDictionary = round.getNumberInDictionary();
		this.beginTime = round.getBeginTime();
		this.beginTimeStr = DateUtils.getDayMonthYearHourMinuteSecondFormat().format(round.getBeginTime()) ;
		this.text = round.getText();
		this.bookAuthor = round.getBookAuthor();
		this.bookName = round.getBookName();

		List<PlayerRoundResult> results = round.getResults();
		results.sort(new PlayerRoundResultPlacesComparator());

		this.playerResults = new ArrayList<>();
		for (PlayerRoundResult result : results) {
			this.playerResults.add( new PlayerRoundResultInfo(result) );
		}

		// links to neighbour rounds
		this.hasPreviousRound = (this.number > Round.FIRST_ROUND_NUMBER);
		if (Boolean.TRUE.equals(this.hasPreviousRound)) { // ugly null-safe check, hail Sonar
			this.previousRoundNumber = (this.number - 1);
		}

		this.hasNextRound = (this.number < (Round.FIRST_ROUND_NUMBER + competition.getRounds().size() - 1) );
		if (Boolean.TRUE.equals(this.hasNextRound)) { // ugly null-safe check, hail Sonar
			this.nextRoundNumber = (this.number + 1);
		}
	}

	public String getCompetitionName() {
		return competitionName;
	}
	public void setCompetitionName(String competitionName) {
		this.competitionName = competitionName;
	}
	public CompetitionDictionary getDictionary() {
		return dictionary;
	}
	public void setDictionary(CompetitionDictionary dictionary) {
		this.dictionary = dictionary;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public Integer getNumberInDictionary() {
		return numberInDictionary;
	}
	public void setNumberInDictionary(Integer numberInDictionary) {
		this.numberInDictionary = numberInDictionary;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public String getBeginTimeStr() {
		return beginTimeStr;
	}
	public void setBeginTimeStr(String beginTimeStr) {
		this.beginTimeStr = beginTimeStr;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getBookAuthor() {
		return bookAuthor;
	}
	public void setBookAuthor(String bookAuthor) {
		this.bookAuthor = bookAuthor;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public List<PlayerRoundResultInfo> getPlayerResults() {
		return playerResults;
	}
	public void setPlayerResults(List<PlayerRoundResultInfo> playerResults) {
		this.playerResults = playerResults;
	}

	public Boolean getHasPreviousRound() {
		return hasPreviousRound;
	}
	public void setHasPreviousRound(Boolean hasPreviousRound) {
		this.hasPreviousRound = hasPreviousRound;
	}
	public Integer getPreviousRoundNumber() {
		return previousRoundNumber;
	}
	public void setPreviousRoundNumber(Integer previousRoundNumber) {
		this.previousRoundNumber = previousRoundNumber;
	}
	public Boolean getHasNextRound() {
		return hasNextRound;
	}
	public void setHasNextRound(Boolean hasNextRound) {
		this.hasNextRound = hasNextRound;
	}
	public Integer getNextRoundNumber() {
		return nextRoundNumber;
	}
	public void setNextRoundNumber(Integer nextRoundNumber) {
		this.nextRoundNumber = nextRoundNumber;
	}

	private String competitionName;

	private CompetitionDictionary dictionary;

	private Integer number;

	private Integer numberInDictionary;

	private Date beginTime;

	private String beginTimeStr;

	private String text;

	private String bookAuthor;

	private String bookName;

	private List<PlayerRoundResultInfo> playerResults;

	private Boolean hasPreviousRound;
	private Integer previousRoundNumber;

	private Boolean hasNextRound;
	private Integer nextRoundNumber;
}
