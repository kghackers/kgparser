/**
 * User: 1
 * Date: 13.01.2012
 * Time: 23:33:06
 */
package ru.klavogonki.kgparser;

/**
 * Игрок клавогонок.
 */
public class Player implements Comparable<Player>
{
	public Integer getProfileId() {
		return profileId;
	}
	public void setProfileId(Integer profileId) {
		this.profileId = profileId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNormalRecord() {
		return normalRecord;
	}
	public void setNormalRecord(int normalRecord) {
		if (normalRecord <= 0)
			throw new IllegalArgumentException("Record must be > 0");

		this.normalRecord = normalRecord;
	}

	public Rank getRank() {
		return Rank.getRank(normalRecord);
	}
	public String getColor() {
		return Rank.getColor( getRank() );
	}

	public int compareTo(Player o) {
		return this.profileId.compareTo(o.profileId);
	}
	public boolean equals(Object player) {
		if (player instanceof Player)
			return this.profileId.equals( ((Player) player).getProfileId() ); 

		return false;
	}
	public int hashCode() {
		return (profileId != null ? profileId.hashCode() : 0);
	}
	
	/**
	 * @param round заезд
	 * @return <code>true</code> - если игрок принимал участие в заезде,
	 * <code>false</code> - в противном случае.
	 */
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
	 * Рекорд в обычном режиме. Должен быть больше 0.
	 */
	private int normalRecord;
}