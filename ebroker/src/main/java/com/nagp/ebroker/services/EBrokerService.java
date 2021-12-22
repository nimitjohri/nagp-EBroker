package com.nagp.ebroker.services;

import java.time.LocalDateTime;

import com.nagp.ebroker.models.BaseResponse;
import com.nagp.ebroker.models.CustomerDTO;
import com.nagp.ebroker.models.EquityDTO;

public interface EBrokerService {

	public BaseResponse buyEquity(int customerId , int equityId,int numberOfUnits, LocalDateTime currentDate) throws Exception;

	public BaseResponse sellEquity(int customerId, int equityId, int numberOfUnits, LocalDateTime currentDate) throws Exception;

	public Double addFunds(int customerId, Double funds) throws Exception;

	public Double getFunds(int customerId) throws Exception;

	public BaseResponse addCustomer(CustomerDTO customerDTO) throws Exception;

	public BaseResponse addEquity(EquityDTO equityDTO) throws Exception;

}
