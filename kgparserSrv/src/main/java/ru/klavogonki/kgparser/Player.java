package ru.klavogonki.kgparser;

import ru.klavogonki.kgparser.http.UrlConstructor;
import su.opencode.kefir.srv.json.Json;
import su.opencode.kefir.srv.json.JsonObject;

/**
 * Игрок клавогонок.
 */
public class Player extends JsonObject implements Comparable<Player>
{
	public Integer getProfileId() {
		return profileId;
	}
	public void setProfileId(Integer profileId) {
		this.profileId = profileId;
	}
	public String getName() {
		if ( isGuest() )
			return GUEST_NAME;

		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Rank getRank() {
		return rank;
	}
	public void setRank(Rank rank) {
		this.rank = rank;
	}
	public Integer getNormalRecord() {
		return normalRecord;
	}
	public void setNormalRecord(Integer normalRecord) {
		if (normalRecord == null)
		{
			this.normalRecord = null;
			return;
		}

		if (normalRecord <= 0)
			throw new IllegalArgumentException("Record must be > 0");

		this.normalRecord = normalRecord;
	}

	@Json(exclude = true)
	public String getProfileLink() {
		return UrlConstructor.userProfileLink(profileId);
	}

	@Json(exclude = true)
	public Rank getRankByNormalRecord() {
		return Rank.getRankByNormalRecord(normalRecord);
	}

	@Json(exclude = true)
	public String getColor() {
		if ( isGuest() )
			return Rank.GUEST_COLOR;

		return getRank().color;
	}

	@Json(exclude = true)
	public boolean isGuest() {
		return profileId == null;
	}

	@Override
	public int compareTo(Player o) {
		return this.profileId.compareTo(o.profileId);
	}

	@Override
	public boolean equals(Object player) {
		if (player instanceof Player)
			return this.profileId.equals( ((Player) player).getProfileId() );

		return false;
	}

	@Override
	public int hashCode() {
		return (profileId != null ? profileId.hashCode() : 0);
	}

	/**
	 * @param round заезд
	 * @return <code>true</code> - если игрок принимал участие в заезде,
	 * <code>false</code> - в противном случае.
	 */
	@Json(exclude = true)
	public boolean isPresent(Round round) {
		for (PlayerRoundResult result : round.getResults())
			if (result.getPlayer().getProfileId().equals(this.profileId))
				return true;

		return false;
	}

	/**
	 * Код профиля.
	 */
	private Integer profileId;

	/**
	 * Никнейм (логин).
	 */
	private String name;

	/**
	 * Ранг.
	 */
	private Rank rank;

	/**
	 * Рекорд в обычном режиме. Должен быть больше 0.
	 */
	private Integer normalRecord;

	/**
	 * Имя игрока-гостя.
	 */
	public static final String GUEST_NAME = "Гость";
}
