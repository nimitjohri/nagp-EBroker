package com.nagp.ebroker.controllers;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nagp.ebroker.models.BaseResponse;
import com.nagp.ebroker.models.CustomerDTO;
import com.nagp.ebroker.models.EquityDTO;
import com.nagp.ebroker.services.EBrokerService;

@RestController
public class EBrokerController {

	@Autowired
	EBrokerService ebrokerService;

	@PostMapping("/buy/{customerId}/{equityId}/{units}")
	public ResponseEntity<String> buyEquity(@PathVariable(value="customerId") String customerId, @PathVariable("equityId") String equityId, @PathVariable("units") String numberOfUnits) {
		BaseResponse baseResponse = new BaseResponse();
		try {
			baseResponse = ebrokerService.buyEquity(Integer.parseInt(customerId), Integer.parseInt(equityId), Integer.parseInt(numberOfUnits), LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(baseResponse.getMessage(), HttpStatus.OK);
	}

	@PostMapping("/sell/{customerId}/{equityId}/{units}")
	public ResponseEntity<String> sellEquity(@PathVariable(value="customerId") String customerId, @PathVariable("equityId") String equityId, @PathVariable("units") String numberOfUnits) {
		BaseResponse baseResponse = new BaseResponse();
		try {
			baseResponse = ebrokerService.sellEquity(Integer.parseInt(customerId), Integer.parseInt(equityId), Integer.parseInt(numberOfUnits), LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(baseResponse.getMessage(), HttpStatus.OK);
	}

	@PostMapping("/equity")
	public ResponseEntity<String> addEquity(@RequestBody EquityDTO equityDTO) {
		BaseResponse response = new BaseResponse();
		try {
			response = ebrokerService.addEquity(equityDTO);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(response.getMessage(), HttpStatus.OK);
	}

	@PatchMapping("/funds/{customerId}/{fundAmt}")
	public ResponseEntity<String> addFunds(@PathVariable(value = "customerId") int customerId, @PathVariable(value = "fundAmt") Double fundAmt) {
		Double updatedWalletAmount;
		try {
			updatedWalletAmount = ebrokerService.addFunds(customerId, fundAmt);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>("Updated Wallet Amount: " + updatedWalletAmount, HttpStatus.OK);
	}

	@GetMapping("/funds/{id}")
	public ResponseEntity<String> getFunds(@PathVariable(value = "id") Integer customerId) {
		Double updatedWalletAmount;
		try {
			updatedWalletAmount = ebrokerService.getFunds(customerId);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>("Current Wallet Amount: " + updatedWalletAmount, HttpStatus.OK);

	}

	@PostMapping("/customer")
	public ResponseEntity<String> addCustomer(@RequestBody CustomerDTO customerDTO) {
		BaseResponse response = new BaseResponse();
		try {
			response = ebrokerService.addCustomer(customerDTO);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(response.getMessage(), HttpStatus.OK);
	}
}
