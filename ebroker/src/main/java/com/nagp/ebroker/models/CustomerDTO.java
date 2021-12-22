package com.nagp.ebroker.models;

public class CustomerDTO {
    // Map<Equity, Integer> equities;

	// public Map<Equity, Integer> getEquities() {
	// 	return equities;
	// }

	// public void setEquities(Map<Equity, Integer> equities) {
	// 	this.equities = equities;
	// }

	String name;
	Double walletAmount;

    public CustomerDTO() {
    }

    public CustomerDTO(String name, Double walletAmount) {
        this.name = name;
        this.walletAmount = walletAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(Double walletAmount) {
        this.walletAmount = walletAmount;
    }

}
