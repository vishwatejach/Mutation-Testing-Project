package com.example.spebackend.service;

import com.example.spebackend.model.Customer;
import com.example.spebackend.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setName("Test User");
        testCustomer.setEmail("test@example.com");
        testCustomer.setPassword("password123");
    }

    @Test
    void saveNewCustomer_WhenEmailNotExists_ShouldSaveAndReturnSuccessMessage1() {
        // Arrange
        when(customerRepository.findByEmail(testCustomer.getEmail())).thenReturn(Optional.empty());
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        // Act
        String result = customerService.saveNewCustomer(testCustomer);

        // Assert
        assertEquals("User Registration was Successful!!", result);
        verify(customerRepository).findByEmail(testCustomer.getEmail());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void saveNewCustomer_WhenEmailNotExists_ShouldSaveAndReturnSuccessMessage2() {
        // Arrange
        when(customerRepository.findByEmail(testCustomer.getEmail())).thenReturn(Optional.empty());
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        String result = customerService.saveNewCustomer(testCustomer);

        // Assert
        assertEquals("User Registration was Successful!!", result);
        verify(customerRepository).findByEmail(testCustomer.getEmail());
        verify(customerRepository).save(argThat(customer ->
                customer.getPassword().equals("password123")));
    }

    @Test
    void saveNewCustomer_WhenEmailNotExists_ShouldSaveAndReturnSuccessMessage3() {
        // Arrange
        when(customerRepository.findByEmail(testCustomer.getEmail())).thenReturn(Optional.empty());
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        String result = customerService.saveNewCustomer(testCustomer);

        // Assert
        assertEquals("User Registration was Successful!!", result);
        verify(customerRepository).findByEmail(testCustomer.getEmail());
        verify(customerRepository).save(argThat(customer ->
                customer.getEmail().equals("test@example.com")));
    }

    @Test
    void saveNewCustomer_WhenEmailNotExists_ShouldSaveAndReturnSuccessMessage4() {
        // Arrange
        when(customerRepository.findByEmail(testCustomer.getEmail())).thenReturn(Optional.empty());
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        String result = customerService.saveNewCustomer(testCustomer);

        // Assert
        assertEquals("User Registration was Successful!!", result);
        verify(customerRepository).findByEmail(testCustomer.getEmail());
        verify(customerRepository).save(argThat(customer ->
                customer.getName().equals("Test User")));
    }




    @Test
    void saveNewCustomer_WhenEmailExists_ShouldReturnErrorMessage() {
        // Arrange
        when(customerRepository.findByEmail(testCustomer.getEmail())).thenReturn(Optional.of(testCustomer));

        // Act
        String result = customerService.saveNewCustomer(testCustomer);

        // Assert
        assertEquals("User Email ID already exist", result);
        verify(customerRepository).findByEmail(testCustomer.getEmail());
        verify(customerRepository, never()).save(any(Customer.class));
    }


    @Test
    void verifyLogin_WhenCredentialsCorrect_ShouldReturnSuccessMessage() {
        // Arrange
        when(customerRepository.findByEmail(testCustomer.getEmail())).thenReturn(Optional.of(testCustomer));

        // Act
        String result = customerService.verifyLogin(testCustomer);

        // Assert
        assertEquals("Login Successfull!", result);
        verify(customerRepository).findByEmail(testCustomer.getEmail());
    }

    @Test
    void verifyLogin_WhenEmailNotFound_ShouldReturnErrorMessage() {
        // Arrange
        when(customerRepository.findByEmail(testCustomer.getEmail())).thenReturn(Optional.empty());

        // Act
        String result = customerService.verifyLogin(testCustomer);

        // Assert
        assertEquals("Email Not Registered!", result);
        verify(customerRepository).findByEmail(testCustomer.getEmail());
    }

    @Test
    void verifyLogin_WhenPasswordIncorrect_ShouldReturnErrorMessage() {
        // Arrange
        Customer storedCustomer = new Customer();
        storedCustomer.setEmail(testCustomer.getEmail());
        storedCustomer.setPassword("differentPassword");

        when(customerRepository.findByEmail(testCustomer.getEmail())).thenReturn(Optional.of(storedCustomer));

        // Act
        String result = customerService.verifyLogin(testCustomer);

        // Assert
        assertEquals("Incorrect Password!", result);
        verify(customerRepository).findByEmail(testCustomer.getEmail());
    }

    @Test
    void getCustomerFromDb_WhenCustomerExists_ShouldReturnCustomer() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));

        // Act
        Optional<Customer> result = customerService.getCustomerFromDb(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testCustomer, result.get());
        verify(customerRepository).findById(1L);
    }

    @Test
    void getCustomerFromDb_WhenCustomerNotExists_ShouldReturnEmpty() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<Customer> result = customerService.getCustomerFromDb(1L);

        // Assert
        assertFalse(result.isPresent());
        verify(customerRepository).findById(1L);
    }

    @Test
    void findByEmail_WhenCustomerExists_ShouldReturnCustomer() {
        // Arrange
        when(customerRepository.findByEmail(testCustomer.getEmail())).thenReturn(Optional.of(testCustomer));

        // Act
        Customer result = customerService.findByEmail(testCustomer.getEmail());

        // Assert
        assertNotNull(result);
        assertEquals(testCustomer, result);
        verify(customerRepository).findByEmail(testCustomer.getEmail());
    }

    @Test
    void findByEmail_WhenCustomerNotExists_ShouldReturnNull() {
        // Arrange
        when(customerRepository.findByEmail(testCustomer.getEmail())).thenReturn(Optional.empty());

        // Act
        Customer result = customerService.findByEmail(testCustomer.getEmail());

        // Assert
        assertNull(result);
        verify(customerRepository).findByEmail(testCustomer.getEmail());
    }
}