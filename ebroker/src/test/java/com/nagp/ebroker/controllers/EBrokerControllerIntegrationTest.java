package com.nagp.ebroker.controllers;

import java.time.LocalDateTime;

import com.nagp.ebroker.utils.Helper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class EBrokerControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

    @Test
    public void testCreateGetPatch() throws Exception{
		// add equity
        MvcResult equityResponse = mockMvc.perform(MockMvcRequestBuilders.post("/equity")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\r\n    \"name\": \"Can\",\r\n    \"Nav\": 40.0\r\n}"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.content().string("Equity Added Successfully with Id 1"))
		.andReturn();

		Integer equityID = Integer.parseInt(equityResponse.getResponse().getContentAsString().split("Id")[1].trim());

		// add customer
		MvcResult customerResponse = mockMvc.perform(MockMvcRequestBuilders.post("/customer")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\r\n    \"name\": \"Ankit\",\r\n    \"walletAmount\": 400.0\r\n}"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.content().string(org.hamcrest.Matchers.containsString("Customer Added Successfully with Id")))
		.andReturn();

		Integer customerId = Integer.parseInt(customerResponse.getResponse().getContentAsString().split("Id")[1].trim());

		// get funds
		mockMvc.perform(MockMvcRequestBuilders.get("/funds/"+ customerId))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.content().string("Current Wallet Amount: 400.0"));

		// get funds
		mockMvc.perform(MockMvcRequestBuilders.patch("/funds/" + customerId + "/120"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.content().string("Updated Wallet Amount: 520.0"));

		if(Helper.checkTime(LocalDateTime.now())){
			// buy 3 units of equity
			mockMvc.perform(MockMvcRequestBuilders.post("/buy/" + customerId + "/" + equityID + "/3"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.content().string("Equity Purchased successfully"));
		} else {
			// buy 3 units of equity
			mockMvc.perform(MockMvcRequestBuilders.post("/buy/" + customerId + "/" + equityID + "/3"))
			.andExpect(MockMvcResultMatchers.status().isInternalServerError())
			.andDo(MockMvcResultHandlers.print());
		}

		if(Helper.checkTime(LocalDateTime.now())){
			// buy 3 units of equity
			mockMvc.perform(MockMvcRequestBuilders.post("/buy/" + customerId + "/" + equityID + "/3"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.content().string("Equity Purchased successfully"));
		} else {
			// buy 3 units of equity
			mockMvc.perform(MockMvcRequestBuilders.post("/buy/" + customerId + "/" + equityID + "/3"))
			.andExpect(MockMvcResultMatchers.status().isInternalServerError())
			.andDo(MockMvcResultHandlers.print());
		}

		if(Helper.checkTime(LocalDateTime.now())){
			// sell 3 units of equity
			mockMvc.perform(MockMvcRequestBuilders.post("/sell/" + customerId + "/" + equityID + "/1"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.content().string("Equity Purchased successfully"));
		} else {
			// sell 3 units of equity
			mockMvc.perform(MockMvcRequestBuilders.post("/sell/" + customerId + "/" + equityID + "/3"))
			.andExpect(MockMvcResultMatchers.status().isInternalServerError())
			.andDo(MockMvcResultHandlers.print());
		}

	}


}
