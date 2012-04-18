package com.example.domain;


import javax.persistence.Entity;
import java.io.Serializable;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Version;
import java.lang.Override;
import javax.persistence.OneToOne;
import com.example.domain.User;
import java.util.ArrayList;@Entity public class Player implements java.io.Serializable {

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
	private User user;

	public User getUser() {
		return this.user;
	}

	public void setUser(final User user) {
		this.user = user;
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
	private ArrayList friendList;

	public ArrayList getFriendList() {
		return this.friendList;
	}

	public void setFriendList(final ArrayList friendList) {
		this.friendList = friendList;
	}

	public String toString() {
		String result = "";
		result += points;
		return result;
	} }