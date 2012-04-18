package com.example.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity
public class User implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "userId", updatable = false, nullable = false, unique = true)
	Long userId = null;

	@Version
	private @Column(name = "version")
	int version = 0;

	public Long getuserId() {
		return this.userId;
	}

	public void setuserId(final Long userId) {
		this.userId = userId;
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
		if (userId != null) {
			return userId.equals(((User) that).userId);
		}
		return super.equals(that);
	}

	@Override
	public int hashCode() {
		if (userId != null) {
			return userId.hashCode();
		}
		return super.hashCode();
	}

	@Column
	private long facebookID;

	public long getFacebookID() {
		return this.facebookID;
	}

	public void setFacebookID(final long facebookID) {
		this.facebookID = facebookID;
	}

	@Column
	private String name;

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Column
	private String imageURL;

	public String getImageURL() {
		return this.imageURL;
	}

	public void setImageURL(final String imageURL) {
		this.imageURL = imageURL;
	}

	public User(long facebookID, String name, String imageURL) {
		this.facebookID = facebookID;
		this.name = name;
		this.imageURL = imageURL;
	}

	public User() {
	}

	public String toString() {
		return "" + facebookID + ", " + name + ", " + imageURL;
	}
}
