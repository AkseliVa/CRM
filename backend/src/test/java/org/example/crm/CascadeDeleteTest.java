package org.example.crm;

import org.example.crm.entities.*;
import org.example.crm.enums.CompanyIndustryType;
import org.example.crm.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CascadeDeleteTest {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CustomerNoteRepository customerNoteRepository;

    @Test
    void testCompanyDeleteCascadesToCustomersAndTickets() {
        // Find a company that has customers and tickets (from DataInitializer)
        Company techCorp = companyRepository.findAll().stream()
                .filter(c -> c.getName().equals("Tech Corp"))
                .findFirst()
                .orElseThrow();

        long companyId = techCorp.getId();
        List<Customer> customers = customerRepository.findAll().stream()
                .filter(c -> c.getCompany().getId().equals(companyId))
                .toList();
        List<Ticket> tickets = ticketRepository.findAll().stream()
                .filter(t -> t.getCompany() != null && t.getCompany().getId().equals(companyId))
                .toList();

        assertThat(customers).isNotEmpty();
        assertThat(tickets).isNotEmpty();

        // Delete the company
        companyRepository.delete(techCorp);

        // Verify cascading deletes
        assertThat(companyRepository.findById(companyId)).isEmpty();
        
        // These are expected to FAIL if cascading is not implemented
        assertThat(customerRepository.findAll().stream().anyMatch(c -> c.getCompany().getId().equals(companyId))).isFalse();
        assertThat(ticketRepository.findAll().stream().anyMatch(t -> t.getCompany() != null && t.getCompany().getId().equals(companyId))).isFalse();
    }

    @Test
    void testCustomerDeleteCascadesToNotesAndTickets() {
        // Find a customer that has notes and tickets
        Customer alice = customerRepository.findAll().stream()
                .filter(c -> c.getName().equals("Alice Brown"))
                .findFirst()
                .orElseThrow();

        long customerId = alice.getId();
        List<CustomerNote> notes = customerNoteRepository.findAll().stream()
                .filter(n -> n.getCustomer().getId().equals(customerId))
                .toList();
        List<Ticket> tickets = ticketRepository.findAll().stream()
                .filter(t -> t.getCustomer() != null && t.getCustomer().getId().equals(customerId))
                .toList();

        assertThat(notes).isNotEmpty();
        assertThat(tickets).isNotEmpty();

        // Delete the customer
        customerRepository.delete(alice);

        // Verify cascading deletes
        assertThat(customerRepository.findById(customerId)).isEmpty();

        // These are expected to FAIL if cascading is not implemented
        assertThat(customerNoteRepository.findAll().stream().anyMatch(n -> n.getCustomer().getId().equals(customerId))).isFalse();
        assertThat(ticketRepository.findAll().stream().anyMatch(t -> t.getCustomer() != null && t.getCustomer().getId().equals(customerId))).isFalse();
    }
}
