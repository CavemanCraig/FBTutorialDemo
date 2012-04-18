package com.example.domain;

import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Version;

@Entity
public class Player implements java.io.Serializable {

	private static final long serialVersionUID = 7884292690096443611L;

	@Id
	private @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	Long id = null;

	@Version
	private @Column(name = "version")
	int version = 0;

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(final int version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object that) {
		if (this == that) {
			return true;
		}
		if (that == null) {
			return false;
		}
		if (getClass() != that.getClass()) {
			return false;
		}
		if (id != null) {
			return id.equals(((Player) that).id);
		}
		return super.equals(that);
	}

	@Override
	public int hashCode() {
		if (id != null) {
			return id.hashCode();
		}
		return super.hashCode();
	}

	@OneToOne
	@JoinColumn(name = "userId")
	private User playerInfo;

	public User getPlayerInfo() {
		return this.playerInfo;
	}

	public void setPlayerInfo(final User playerInfo) {
		this.playerInfo = playerInfo;
	}

	@Column
	private long points;

	public long getPoints() {
		return this.points;
	}

	public void setPoints(final long points) {
		this.points = points;
	}

	@Column
	private ArrayList<Long> friendList;

	public ArrayList<Long> getFriendList() {
		return this.friendList;
	}

	public void setFriendList(final ArrayList<Long> friendList) {
		this.friendList = friendList;
	}

	public Player() {
	}

	public Player(User playerInfo) {
		this.playerInfo = playerInfo;
		this.points = 100; // New users get 100 points to start with
		this.friendList = new ArrayList<Long>();
	}

	public String toString() {
		return playerInfo.getName() + ", " + points + ", FrindIDs: ["
				+ friendList + "]";
	}
}
