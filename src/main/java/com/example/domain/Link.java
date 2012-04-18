package com.example.domain;

import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity
public class Link {

	private static final long serialVersionUID = 31568243611L;

	@Id
	private @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	Long linkId = null;

	@Version
	private @Column(name = "version")
	int version = 0;

	public Long getLinkId() {
		return this.linkId;
	}

	public void setLinkId(final Long linkId) {
		this.linkId = linkId;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(final int version) {
		this.version = version;
	}

	@Column
	private String href;

	public String getHref() {
		return this.href;
	}

	public void setHref(final String href) {
		this.href = href;
	}

	@Column
	private String onClickMethod;

	public String getOnClickMethod() {
		return this.onClickMethod;
	}

	public void setOnClickMethod(final String onClickMethod) {
		this.onClickMethod = onClickMethod;
	}

	public Link() {
	}

	public Link(String href) {
		this.href = href;
		this.onClickMethod = null;
	}

	public Link(String href, String onClickMethod) {
		this.href = href;
		this.onClickMethod = onClickMethod;
	}

	public String toString() {
		return "href: " + this.href + ", onClickMethod: " + this.onClickMethod;
	}

}