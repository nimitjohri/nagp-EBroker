package com.nagp.ebroker.entities;

import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int id;

	@ElementCollection
	Map<Equity, Integer> equities;

	String name;

	Double walletAmount;

	public Customer() {
	}

	public Customer( String name, Double walletAmount) {
		this.name = name;
		this.walletAmount = walletAmount;
	}

	public int getId() {
		return id;
	}

	public Map<Equity, Integer> getEquities() {
		return equities;
	}

	public void setEquities(Map<Equity, Integer> equities) {
		this.equities = equities;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Double getWalletAmount() {
		return walletAmount;
	}

	public void setWalletAmount(Double walletAmount) {
		this.walletAmount = walletAmount;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Customer [id=");
		builder.append(id);
		builder.append(", equities=");
		builder.append(equities);
		builder.append(", walletAmount=");
		builder.append(walletAmount);
		builder.append("]");
		return builder.toString();
	}

}
