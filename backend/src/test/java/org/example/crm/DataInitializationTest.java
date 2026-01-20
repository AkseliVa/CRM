package org.example.crm;

import org.example.crm.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DataInitializationTest {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CustomerNoteRepository customerNoteRepository;

    @Test
    void testDataIsInitialized() {
        assertThat(userRepository.count()).isEqualTo(3);
        assertThat(companyRepository.count()).isEqualTo(3);
        assertThat(customerRepository.count()).isEqualTo(4);
        assertThat(ticketRepository.count()).isEqualTo(4);
        assertThat(customerNoteRepository.count()).isEqualTo(3);
    }
}
