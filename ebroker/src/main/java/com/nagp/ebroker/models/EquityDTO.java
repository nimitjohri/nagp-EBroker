package com.nagp.ebroker.models;

public class EquityDTO {
    String name;
	double nav;

	public EquityDTO() {
	}

	public EquityDTO(String name, double nav) {
		this.name = name;
		this.nav = nav;
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

}
