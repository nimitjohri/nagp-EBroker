package com.nagp.ebroker.services.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagp.ebroker.entities.Customer;
import com.nagp.ebroker.entities.Equity;
import com.nagp.ebroker.models.BaseResponse;
import com.nagp.ebroker.models.CustomerDTO;
import com.nagp.ebroker.models.EquityDTO;
import com.nagp.ebroker.repositories.CustomerRepository;
import com.nagp.ebroker.repositories.EquityRepository;
import com.nagp.ebroker.services.EBrokerService;
import com.nagp.ebroker.utils.Helper;

@Service
public class EBrokerServiceImpl implements EBrokerService {

	@Autowired
	CustomerRepository customerRepo;

	@Autowired
	EquityRepository equityRepo;

	@Override
	public BaseResponse addEquity(EquityDTO equityDTO) throws Exception {
		Equity equity = new Equity(equityDTO.getName(), equityDTO.getNav());
		Equity savedEquity = equityRepo.save(equity);
		if(Objects.isNull(savedEquity)){
			throw new Exception("Equity Adding failed");
		}
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatus("SUCCESS");
		baseResponse.setMessage(String.format("Equity Added Successfully with Id %s", savedEquity.getId()));
		return baseResponse;
	}

	@Override
	public BaseResponse buyEquity(int customerId, int equityId, int numberOfUnits, LocalDateTime currentDate) throws Exception {
		if (Helper.checkTime(currentDate)) {
			Optional<Customer> customer = customerRepo.findById(customerId);
			if (customer.isPresent()) {
				Optional<Equity> equity = equityRepo.findById(equityId);
				if (equity.isPresent()) {
					double currentWalletAmount = customer.get().getWalletAmount();
					double transactionAmount = equity.get().getNav() * numberOfUnits;
					if (currentWalletAmount >= transactionAmount) {
						customer.get().setWalletAmount(currentWalletAmount - transactionAmount);
						Map<Equity,Integer> equityList = customer.get().getEquities();
						if (Objects.isNull(equityList)) {
							equityList = new HashMap<>();
						}
						equityList.put(equity.get(), numberOfUnits);
						customer.get().setEquities(equityList);
						customerRepo.save(customer.get());
						BaseResponse baseResponse = new BaseResponse();
						baseResponse.setStatus("SUCCESS");
						baseResponse.setMessage("Equity Purchased successfully");
						return baseResponse;
					} else
						throw new Exception("Insufficient wallet amount");
				} else
					throw new Exception("Invalid equity Id");
			} else
				throw new Exception("Invalid Customer Id");
		} else
			throw new Exception("Please try between 9AM to 5PM (MON-FRI)");
	}

	@Override
	public BaseResponse sellEquity(int customerId, int equityId, int numberOfUnits, LocalDateTime currentDate) throws Exception {
		if(Helper.checkTime(currentDate)) {
			Optional<Customer> customer = customerRepo.findById(customerId);
			if (customer.isPresent()) {
				Map<Equity, Integer> equities = customer.get().getEquities();
				if(equities.keySet().stream().anyMatch(e -> e.getId() == equityId)){
					Optional<Equity> ownedEquity = equities.keySet().stream().findAny().filter(e -> e.getId() == equityId);
					Integer ownedQuantites = equities.get(ownedEquity.get());
					if(ownedQuantites > numberOfUnits){
						double amountFromSelling = ownedEquity.get().getNav() * numberOfUnits;
						double updatedWalletAmount = customer.get().getWalletAmount() + amountFromSelling;
						equities.put(ownedEquity.get(), ownedQuantites-numberOfUnits);
						customer.get().setEquities(equities);
						customer.get().setWalletAmount(updatedWalletAmount);
						customerRepo.save(customer.get());
						BaseResponse baseResponse = new BaseResponse();
						baseResponse.setStatus("SUCCESS");
						baseResponse.setMessage("Equity Selled successfully");
						return baseResponse;
					}else
						throw new Exception("Customer does not owned required units of the Equity");
				} else
					throw new Exception("Customer Does not own this equity");
			} else
				throw new Exception("Invalid Customer Id");
		} else
			throw new Exception("Please try between 9AM to 5PM (MON-FRI)");

	}

	@Override
	public Double addFunds(int customerId, Double funds) throws Exception {
		Optional<Customer> customer = customerRepo.findById(customerId);
		if (customer.isPresent()) {
			Double currentWalletAmount = customer.get().getWalletAmount();
			Double updatedWalletAmount = currentWalletAmount + funds;
			customer.get().setWalletAmount(updatedWalletAmount);
			return updatedWalletAmount;
		} else {
			throw new Exception("Invalid Customer ID");
		}
	}

	@Override
	public Double getFunds(int customerId) throws Exception {
		Optional<Customer> customer = customerRepo.findById(customerId);
		if (customer.isPresent()) {
			return customer.get().getWalletAmount();
		} else {
			throw new Exception("Invalid Customer ID");
		}
	}

	@Override
	public BaseResponse addCustomer(CustomerDTO customerDTO) throws Exception {
		Customer customer = new Customer(customerDTO.getName(), customerDTO.getWalletAmount());
		Customer savedCustomer = customerRepo.save(customer);
		if(Objects.isNull(savedCustomer)){
			throw new Exception("Customer Adding failed");
		}
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatus("SUCCESS");
		baseResponse.setMessage(String.format("Customer Added Successfully with Id %s", savedCustomer.getId()));
		return baseResponse;
	}

}
