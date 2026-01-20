package org.example.crm.config;

import org.example.crm.entities.*;
import org.example.crm.enums.*;
import org.example.crm.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(CompanyRepository companyRepository,
                                      CustomerRepository customerRepository,
                                      UserRepository userRepository,
                                      TicketRepository ticketRepository,
                                      CustomerNoteRepository customerNoteRepository) {
        return args -> {
            System.out.println("Initializing test data...");

            // Create Users
            User admin = new User("admin@example.com", UserRole.ADMIN, "password", LocalDateTime.now(), LocalDateTime.now());
            User user1 = new User("user1@example.com", UserRole.USER, "password", LocalDateTime.now(), LocalDateTime.now());
            User user2 = new User("user2@example.com", UserRole.USER, "password", LocalDateTime.now(), LocalDateTime.now());
            userRepository.saveAll(Arrays.asList(admin, user1, user2));

            // Create Companies
            Company techCorp = new Company("Tech Corp", CompanyIndustryType.IT, LocalDateTime.now(), LocalDateTime.now());
            Company financeInc = new Company("Finance Inc", CompanyIndustryType.FINANCE, LocalDateTime.now(), LocalDateTime.now());
            Company marketingPro = new Company("Marketing Pro", CompanyIndustryType.MARKETING, LocalDateTime.now(), LocalDateTime.now());
            companyRepository.saveAll(Arrays.asList(techCorp, financeInc, marketingPro));

            // Create Customers
            Customer john = new Customer(techCorp, "John Doe", "john@techcorp.com", "123-456-7890", LocalDateTime.now(), LocalDateTime.now());
            Customer jane = new Customer(techCorp, "Jane Smith", "jane@techcorp.com", "098-765-4321", LocalDateTime.now(), LocalDateTime.now());
            Customer bob = new Customer(financeInc, "Bob Wilson", "bob@financeinc.com", "555-0123", LocalDateTime.now(), LocalDateTime.now());
            Customer alice = new Customer(marketingPro, "Alice Brown", "alice@marketingpro.com", "555-4567", LocalDateTime.now(), LocalDateTime.now());
            customerRepository.saveAll(Arrays.asList(john, jane, bob, alice));

            // Create Tickets
            Ticket t1 = new Ticket("Server Downtime", "Main server is down in Tech Corp", TicketPriority.HIGH, TicketStatus.OPEN, LocalDateTime.now(), LocalDateTime.now());
            t1.setCompany(techCorp);
            t1.setCustomer(john);
            t1.setAssignedUser(user1);

            Ticket t2 = new Ticket("Billing Inquiry", "Question about last month invoice", TicketPriority.LOW, TicketStatus.IN_PROGRESS, LocalDateTime.now(), LocalDateTime.now());
            t2.setCompany(financeInc);
            t2.setCustomer(bob);
            t2.setAssignedUser(user2);

            Ticket t3 = new Ticket("Feature Request", "Requesting new dashboard features", TicketPriority.MEDIUM, TicketStatus.OPEN, LocalDateTime.now(), LocalDateTime.now());
            t3.setCompany(marketingPro);
            t3.setCustomer(alice);
            t3.setAssignedUser(admin);

            Ticket t4 = new Ticket("Password Reset", "Customer forgot password", TicketPriority.LOW, TicketStatus.CLOSED, LocalDateTime.now(), LocalDateTime.now());
            t4.setCompany(techCorp);
            t4.setCustomer(jane);
            t4.setAssignedUser(user1);

            ticketRepository.saveAll(Arrays.asList(t1, t2, t3, t4));

            // Create Customer Notes
            CustomerNote n1 = new CustomerNote(john, user1, "Called customer regarding downtime, they are very frustrated.", LocalDateTime.now(), LocalDateTime.now());
            CustomerNote n2 = new CustomerNote(bob, user2, "Sent invoice details to customer.", LocalDateTime.now(), LocalDateTime.now());
            CustomerNote n3 = new CustomerNote(alice, admin, "Customer is interested in premium plan.", LocalDateTime.now(), LocalDateTime.now());
            
            customerNoteRepository.saveAll(Arrays.asList(n1, n2, n3));

            System.out.println("Test data initialized successfully.");
        };
    }
}
