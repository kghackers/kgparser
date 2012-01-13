/**
 * User: 1
 * Date: 13.01.2012
 * Time: 23:33:06
 */
package ru.klavogonki.kgparser;

/**
 * Игрок клавогонок.
 */
public class Player
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
	
	/**
	 * Код профиля.
	 */
	private Integer profileId;

	/**
	 * Никнейм (логин).
	 */
	private String name;
}