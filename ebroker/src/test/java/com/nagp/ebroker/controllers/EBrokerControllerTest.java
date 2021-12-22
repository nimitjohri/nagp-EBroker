package com.nagp.ebroker.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagp.ebroker.models.BaseResponse;
import com.nagp.ebroker.models.CustomerDTO;
import com.nagp.ebroker.models.EquityDTO;
import com.nagp.ebroker.services.EBrokerService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(EBrokerController.class)
public class EBrokerControllerTest {

	@MockBean
	private EBrokerService eBrokerService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testGetFunds() throws Exception {
		Double expectedFund = (double) 500;
		Mockito.when(eBrokerService.getFunds(Mockito.anyInt())).thenReturn(expectedFund);
		mockMvc.perform(MockMvcRequestBuilders.get("/funds/1")).andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.content().string("Current Wallet Amount: 500.0"));

	}

	@Test
	public void testGetFundsThrowsException() throws Exception {
		Mockito.when(eBrokerService.getFunds(Mockito.anyInt())).thenThrow(new Exception("Error while fetching Customer"));
		mockMvc.perform(MockMvcRequestBuilders.get("/funds/1"))
		.andExpect(MockMvcResultMatchers.status().isInternalServerError())
		.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.content().string("Error while fetching Customer"));
	}

	@Test
	public void testAddFunds() throws Exception {
		Double expectedFund = (double) 500;
		Mockito.when(eBrokerService.addFunds(Mockito.anyInt(), Mockito.anyDouble())).thenReturn(expectedFund);
		mockMvc.perform(MockMvcRequestBuilders.patch("/funds/1/100")).andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.content().string("Updated Wallet Amount: 500.0"));
	}

	@Test
	public void testAddFundsThrowsException() throws Exception {
		Mockito.when(eBrokerService.addFunds(Mockito.anyInt(), Mockito.anyDouble())).thenThrow(new Exception("Error while fetching Customer"));
		mockMvc.perform(MockMvcRequestBuilders.patch("/funds/1/100"))
		.andExpect(MockMvcResultMatchers.status().isInternalServerError())
		.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.content().string("Error while fetching Customer"));
	}


	@Test
	public void testbuyEquity() throws Exception {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setMessage("Equity Purchased successfully");
		baseResponse.setStatus("SUCCESS");
        
		Mockito.when(eBrokerService.buyEquity(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.any())).thenReturn(baseResponse);
		mockMvc.perform(MockMvcRequestBuilders.post("/buy/1/101/2"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.content().string("Equity Purchased successfully"));

	}

	@Test
	public void testbuyEquityThrowsException() throws Exception {
		Mockito.when(eBrokerService.buyEquity(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.any())).thenThrow(new Exception("Invalid Customer ID"));
		mockMvc.perform(MockMvcRequestBuilders.post("/buy/1/101/2"))
		.andExpect(MockMvcResultMatchers.status().isInternalServerError())
		.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.content().string("Invalid Customer ID"));

	}

	@Test
	public void testSellEquity() throws Exception {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setMessage("Equity selled successfully");
		baseResponse.setStatus("SUCCESS");
		Mockito.when(eBrokerService.sellEquity(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.any())).thenReturn(baseResponse);
		mockMvc.perform(MockMvcRequestBuilders.post("/sell/1/101/2"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.content().string("Equity selled successfully"));
	}

	@Test
	public void testSellEquityThrowsException() throws Exception {
		Mockito.when(eBrokerService.sellEquity(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.any())).thenThrow(new Exception("Invalid Customer ID"));
		mockMvc.perform(MockMvcRequestBuilders.post("/sell/1/101/2"))
		.andExpect(MockMvcResultMatchers.status().isInternalServerError())
		.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.content().string("Invalid Customer ID"));

	}

	@Test
	public void testAddEquity() throws Exception {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setMessage("Equity Added Successfully");
		baseResponse.setStatus("SUCCESS");
		EquityDTO equityDTO = new EquityDTO("Can", 40.0);
		Mockito.when(eBrokerService.addEquity(Mockito.any())).thenReturn(baseResponse);
		mockMvc.perform(MockMvcRequestBuilders.post("/equity")
		.contentType(MediaType.APPLICATION_JSON)
		.content(new ObjectMapper().writeValueAsString(equityDTO)))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.content().string("Equity Added Successfully"));
	}

	@Test
	public void testAddEquityThrowsException() throws Exception {
		EquityDTO equityDTO = new EquityDTO("Can", 40.0);
		Mockito.when(eBrokerService.addEquity(Mockito.any())).thenThrow(new Exception("Equity Addition failed"));
		mockMvc.perform(MockMvcRequestBuilders.post("/equity")
		.contentType(MediaType.APPLICATION_JSON)
		.content(new ObjectMapper().writeValueAsString(equityDTO)))
		.andExpect(MockMvcResultMatchers.status().isInternalServerError())
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.content().string("Equity Addition failed"));
	}

	@Test
	public void testAddCustomer() throws Exception {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setMessage("Customer Added Successfully");
		baseResponse.setStatus("SUCCESS");
		CustomerDTO customerDTO = new CustomerDTO("Nimit", 400.0);
		Mockito.when(eBrokerService.addCustomer(Mockito.any())).thenReturn(baseResponse);
		mockMvc.perform(MockMvcRequestBuilders.post("/customer")
		.contentType(MediaType.APPLICATION_JSON)
		.content(new ObjectMapper().writeValueAsString(customerDTO)))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.content().string("Customer Added Successfully"));
	}

	@Test
	public void testAddCustomerThrowException() throws Exception {
		CustomerDTO customerDTO = new CustomerDTO("Nimit", 400.0);
		Mockito.when(eBrokerService.addCustomer(Mockito.any())).thenThrow(new Exception("Customer Addition failed"));
		mockMvc.perform(MockMvcRequestBuilders.post("/customer")
		.contentType(MediaType.APPLICATION_JSON)
		.content(new ObjectMapper().writeValueAsString(customerDTO)))
		.andExpect(MockMvcResultMatchers.status().isInternalServerError())
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.content().string("Customer Addition failed"));
	}


	@AfterEach
	private void teadDown(){
		Mockito.reset(eBrokerService);
	}

}
