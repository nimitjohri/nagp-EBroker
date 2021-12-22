package com.nagp.ebroker.repositories;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.nagp.ebroker.entities.Customer;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;


    @Test
    public void testGetCustomer() {
        Customer customer =new Customer();
        customer.setId(1);
        customer.setWalletAmount((double) 400);
        customerRepository.save(customer);

        Customer result = customerRepository.getById(1);
        Assertions.assertThat(result.getId()).isEqualTo(1);
    }
}
