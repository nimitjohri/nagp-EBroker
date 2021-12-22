package com.nagp.ebroker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nagp.ebroker.entities.Equity;

public interface EquityRepository extends JpaRepository<Equity, Integer> {

}
