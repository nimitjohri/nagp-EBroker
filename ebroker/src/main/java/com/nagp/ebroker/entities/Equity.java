package com.nagp.ebroker.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Equity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int id;
	String name;
	double nav;

	public Equity() {
	}

	public Equity(String name, double nav) {
		this.name = name;
		this.nav = nav;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getNav() {
		return nav;
	}

	public void setNav(double nav) {
		this.nav = nav;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Equity [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", nav=");
		builder.append(nav);
		builder.append("]");
		return builder.toString();
	}

}
