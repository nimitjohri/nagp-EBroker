package com.nagp.ebroker.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nagp.ebroker.entities.Customer;
import com.nagp.ebroker.entities.Equity;
import com.nagp.ebroker.models.BaseResponse;
import com.nagp.ebroker.models.CustomerDTO;
import com.nagp.ebroker.models.EquityDTO;
import com.nagp.ebroker.repositories.CustomerRepository;
import com.nagp.ebroker.repositories.EquityRepository;
import com.nagp.ebroker.utils.Helper;

@ExtendWith(MockitoExtension.class)
public class EBrokerServiceImplTest {

	@InjectMocks
	private EBrokerServiceImpl eBrokerService;

	@Mock
	private CustomerRepository customerRepo;
    
    @Mock
    private Helper mockhelper;

	@Mock
	private EquityRepository equityRepo;
    private MockedStatic<Helper> helper;

    @BeforeEach
    private void setup(){
        helper= Mockito.mockStatic(Helper.class);
    }

    @DisplayName("Should save equity and return equity ID")
	@Test
	public void shouldSaveEquityAndReturnEquityID() throws Exception {
		EquityDTO equityDTO = new EquityDTO("Can", 40.0);

        Equity equity = new Equity();
		equity.setId(1);
        equity.setName("Can");
		equity.setNav((double) 400);
		Mockito.when(equityRepo.save(Mockito.any())).thenReturn(equity);
		BaseResponse response = eBrokerService.addEquity(equityDTO);
		assertEquals("Equity Added Successfully with Id 1", response.getMessage());
		assertEquals("SUCCESS", response.getStatus());
	}

    @DisplayName("Should throw exception while saving equity")
	@Test
	public void shouldThrowExceptionWhileSavingEquity() throws Exception {
		EquityDTO equityDTO = new EquityDTO("Can", 40.0);

		Mockito.when(equityRepo.save(Mockito.any())).thenReturn(null);
        Exception exception = assertThrows(Exception.class,() -> eBrokerService.addEquity(equityDTO));
		assertEquals("Equity Adding failed", exception.getMessage());
	}


    @DisplayName("Should be able to buy equity")
	@Test
	public void shouldBeAbleToBuyEquity() throws Exception {
		Customer customer = new Customer();
		customer.setId(1);
		customer.setWalletAmount((double) 500);

        Equity equity = new Equity();
        equity.setId(101);
        equity.setNav((double) 40);
        LocalDateTime localDateTime = LocalDateTime.of(2021, 12, 21, 10, 1);
        
        helper.when(()->Helper.checkTime(localDateTime)).thenReturn(true);
        Mockito.when(customerRepo.findById(Mockito.any())).thenReturn(Optional.of(customer));
        Mockito.when(equityRepo.findById(Mockito.any())).thenReturn(Optional.of(equity));
		
        BaseResponse baseResponse = eBrokerService.buyEquity(1, 101, 2, localDateTime);
		assertEquals("Equity Purchased successfully", baseResponse.getMessage());
		assertEquals("SUCCESS", baseResponse.getStatus());
        assertEquals(1, customer.getEquities().size());
        // verify num of units
        assertEquals(2, customer.getEquities().get(equity));
	}

    @DisplayName("Should throw exception if time is not valid")
	@Test
	public void shouldThrowExceptionIfTimeIsNotValid() throws Exception {
        
        LocalDateTime localDateTime = LocalDateTime.of(2021, 12, 21, 8, 1);
        
        helper.when(()->Helper.checkTime(localDateTime)).thenReturn(false);
		
		Exception exception = assertThrows(Exception.class, () -> eBrokerService.buyEquity(1, 101, 2, localDateTime));
        assertEquals("Please try between 9AM to 5PM (MON-FRI)", exception.getMessage());
	}

    @DisplayName("should Throw Excetion If Customer Does Not Exists")
	@Test
	public void shouldThrowExcetionIfCustomerDoesNotExists() throws Exception { 
        
        LocalDateTime localDateTime = LocalDateTime.of(2021, 12, 21, 10, 1);
        
        helper.when(()->Helper.checkTime(localDateTime)).thenReturn(true);
        Mockito.when(customerRepo.findById(Mockito.any())).thenReturn(Optional.<Customer>empty());
		Exception exception = assertThrows(Exception.class, () -> eBrokerService.buyEquity(1, 101, 2, localDateTime));
        assertEquals("Invalid Customer Id", exception.getMessage());
	}

    @DisplayName("should Throw Excetion If Equity Does Not Exists")
	@Test
	public void shouldThrowExcetionIfEquityDoesNotExists() throws Exception {
		Customer customer = new Customer();
		customer.setId(1);
		customer.setWalletAmount((double) 500);

        Equity equity = new Equity();
        equity.setId(101);
        equity.setNav((double) 40);
       
        LocalDateTime localDateTime = LocalDateTime.of(2021, 12, 21, 10, 1);
        
        helper.when(()->Helper.checkTime(localDateTime)).thenReturn(true);
        Mockito.when(customerRepo.findById(Mockito.any())).thenReturn(Optional.of(customer));
        Mockito.when(equityRepo.findById(Mockito.any())).thenReturn(Optional.<Equity>empty());
		
		Exception exception = assertThrows(Exception.class, () -> eBrokerService.buyEquity(1, 102, 2, localDateTime));
        assertEquals("Invalid equity Id", exception.getMessage());
	}

    @DisplayName("should Throw Excetion If Wallet Amount Is Less")
	@Test
	public void shouldThrowExcetionIfWalletAmountIsLess() throws Exception {
		Customer customer = new Customer();
		customer.setId(1);
		customer.setWalletAmount((double) 50);

        Equity equity = new Equity();
        equity.setId(101);
        equity.setNav((double) 40);
        
        
        LocalDateTime localDateTime = LocalDateTime.of(2021, 12, 21, 10, 1);
        
        helper.when(()->Helper.checkTime(localDateTime)).thenReturn(true);
        Mockito.when(customerRepo.findById(Mockito.any())).thenReturn(Optional.of(customer));
        Mockito.when(equityRepo.findById(Mockito.any())).thenReturn(Optional.of(equity));
		
		Exception exception = assertThrows(Exception.class, () -> eBrokerService.buyEquity(1, 101, 2, localDateTime));
        assertEquals("Insufficient wallet amount", exception.getMessage());
        assertEquals(null, customer.getEquities());
	}

    @DisplayName("Should be able to sell equity and wallet amount get updated and quantity of equity decreased")
	@Test
	public void shouldBeAbleToSellEquityAndWalletAmountGetUpdatedAndQuantityOfEquityDecreased() throws Exception {
        Equity equity = new Equity();
        equity.setId(101);
        equity.setNav((double) 40);

        Map<Equity, Integer> equities = new HashMap<>();
        equities.put(equity, 10);
        Customer customer = new Customer();
		customer.setId(1);
		customer.setWalletAmount((double) 500);
        customer.setEquities(equities);

		LocalDateTime localDateTime = LocalDateTime.of(2021, 12, 21, 10, 1);
        helper.when(()->Helper.checkTime(localDateTime)).thenReturn(true);
        Mockito.when(customerRepo.findById(Mockito.any())).thenReturn(Optional.of(customer));
		
        BaseResponse baseResponse = eBrokerService.sellEquity(1, 101, 2, localDateTime);
		assertEquals("Equity Selled successfully", baseResponse.getMessage());
		assertEquals("SUCCESS", baseResponse.getStatus());
        assertEquals(8, customer.getEquities().get(equity));
        assertEquals(580, customer.getWalletAmount());
	}

    @DisplayName("Should throws exception if customer tries to sell equity at weekends")
	@Test
	public void shouldThrowsExceptionIfCustomerTriesToSellEquityAtWeekends() throws Exception {
        
		LocalDateTime localDateTime = LocalDateTime.of(2021, 12, 21, 8, 1);
        helper.when(()->Helper.checkTime(localDateTime)).thenReturn(false);
		
        Exception exception = assertThrows(Exception.class,() -> eBrokerService.sellEquity(1, 101, 2, localDateTime));
        assertEquals(exception.getMessage(), "Please try between 9AM to 5PM (MON-FRI)");
	}

    @DisplayName("Should throws exception if customerId is invalid")
	@Test
	public void shouldThrowsExceptionIfCustomerIdIsInvalid() throws Exception {
		LocalDateTime localDateTime = LocalDateTime.of(2021, 12, 21, 10, 1);
        helper.when(()->Helper.checkTime(localDateTime)).thenReturn(true);
        Mockito.when(customerRepo.findById(Mockito.any())).thenReturn(Optional.<Customer>empty());
		
        Exception exception = assertThrows(Exception.class,() -> eBrokerService.sellEquity(1, 101, 2, localDateTime));
        assertEquals(exception.getMessage(), "Invalid Customer Id");
	}

    @DisplayName("Should throws exception if customer does not own the equity")
	@Test
	public void shouldThrowsExceptionIfCustomerDoesNotOwnTheEquity() throws Exception {
        Equity equity = new Equity();
        equity.setId(101);
        equity.setNav((double) 40);

        Map<Equity, Integer> equities = new HashMap<>();
        equities.put(equity, 10);
        Customer customer = new Customer();
		customer.setId(1);
		customer.setWalletAmount((double) 500);
        customer.setEquities(equities);

		LocalDateTime localDateTime = LocalDateTime.of(2021, 12, 21, 10, 1);
        helper.when(()->Helper.checkTime(localDateTime)).thenReturn(true);
        Mockito.when(customerRepo.findById(Mockito.any())).thenReturn(Optional.of(customer));
		
        Exception exception = assertThrows(Exception.class,() -> eBrokerService.sellEquity(1, 102, 2, localDateTime));
        assertEquals(exception.getMessage(), "Customer Does not own this equity");
    }

    @DisplayName("Should throws exception if customer does not own the required number of equity")
	@Test
	public void shouldThrowsExceptionIfCustomerDoesNotOwnTheRequiredUnitsOfEquity() throws Exception {
        Equity equity = new Equity();
        equity.setId(101);
        equity.setNav((double) 40);

        Map<Equity, Integer> equities = new HashMap<>();
        equities.put(equity, 10);
        Customer customer = new Customer();
		customer.setId(1);
		customer.setWalletAmount((double) 500);
        customer.setEquities(equities);

		LocalDateTime localDateTime = LocalDateTime.of(2021, 12, 21, 10, 1);
        helper.when(()->Helper.checkTime(localDateTime)).thenReturn(true);
        Mockito.when(customerRepo.findById(Mockito.any())).thenReturn(Optional.of(customer));
		
        Exception exception = assertThrows(Exception.class,() -> eBrokerService.sellEquity(1, 101, 20, localDateTime));
        assertEquals(exception.getMessage(), "Customer does not owned required units of the Equity");
	}

    @DisplayName("Should Return Funds")
	@Test
	public void shouldReturnFunds() throws Exception {
		Customer customer = new Customer();
		customer.setId(1);
		customer.setWalletAmount((double) 500);
		Mockito.when(customerRepo.findById(Mockito.any())).thenReturn(Optional.of(customer));
		double fundValue = eBrokerService.getFunds(1);
		assertEquals(500, fundValue);
	}

    @DisplayName("Should throw Exception if customer does not exists")
	@Test
	public void shouldThorwExceptionIfUseDoesNotExists() throws Exception {
		Customer customer = new Customer();
		customer.setId(1);
		customer.setWalletAmount((double) 500);
		Mockito.when(customerRepo.findById(Mockito.any())).thenReturn(Optional.<Customer>empty());
		Exception exception = assertThrows(Exception.class,() -> eBrokerService.getFunds(2));
        assertEquals("Invalid Customer ID", exception.getMessage());
	}

    @DisplayName("Should Return Updated Funds")
	@Test
	public void shouldReturnUpdatedFunds() throws Exception {
		Customer customer = new Customer();
		customer.setId(1);
		customer.setWalletAmount((double) 500);
		Mockito.when(customerRepo.findById(Mockito.any())).thenReturn(Optional.of(customer));
		double fundValue = eBrokerService.addFunds(1, (double) 200);
		assertEquals(700, fundValue);
	}

    @DisplayName("Should Return Exception when Customer does not exists")
	@Test
	public void shouldThorwExceptionIfCustomerDoesNotExists() throws Exception {
		Customer customer = new Customer();
		customer.setId(1);
		customer.setWalletAmount((double) 500);
		Mockito.when(customerRepo.findById(Mockito.any())).thenReturn(Optional.<Customer>empty());
		Exception exception = assertThrows(Exception.class,() -> eBrokerService.addFunds(1, (double) 200));
        assertEquals("Invalid Customer ID", exception.getMessage());
	}

    @DisplayName("Should save customer and return customer ID")
	@Test
	public void shouldSaveCustomerAndReturnCustomerID() throws Exception {
		CustomerDTO customerDTO = new CustomerDTO("Nimit", 400.0);

        Customer customer = new Customer();
		customer.setId(1);
		customer.setWalletAmount((double) 500);
		Mockito.when(customerRepo.save(Mockito.any())).thenReturn(customer);
		BaseResponse response = eBrokerService.addCustomer(customerDTO);
		assertEquals("Customer Added Successfully with Id 1", response.getMessage());
		assertEquals("SUCCESS", response.getStatus());
	}


    @DisplayName("Should throw exception while saving customer")
	@Test
	public void shouldThrowExceptionWhileSavingCustomer() throws Exception {
		CustomerDTO customerDTO = new CustomerDTO("Nimit", 400.0);

		Mockito.when(customerRepo.save(Mockito.any())).thenReturn(null);
        Exception exception = assertThrows(Exception.class,() -> eBrokerService.addCustomer(customerDTO));
		assertEquals("Customer Adding failed", exception.getMessage());
	}


    @AfterEach
    public void tearDown(){
        System.out.println("Should execute after each test case.");
        Mockito.reset(customerRepo);
        Mockito.reset(equityRepo);
        helper.close();
        // Mockito.reset(helper);
    }

    @AfterAll
    public static void tearDownAll(){
        System.out.println("Should be executed at end of all test cases.");
    }

}
