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
            User user3 = new User("user3@example.com", UserRole.USER, "password", LocalDateTime.now(), LocalDateTime.now());
            User user4 = new User("user4@example.com", UserRole.USER, "password", LocalDateTime.now(), LocalDateTime.now());
            userRepository.saveAll(Arrays.asList(admin, user1, user2, user3, user4));

            // Create Companies
            Company techCorp = new Company("Tech Corp", CompanyIndustryType.IT, LocalDateTime.now(), LocalDateTime.now());
            Company financeInc = new Company("Finance Inc", CompanyIndustryType.FINANCE, LocalDateTime.now(), LocalDateTime.now());
            Company marketingPro = new Company("Marketing Pro", CompanyIndustryType.MARKETING, LocalDateTime.now(), LocalDateTime.now());
            Company healthPlus = new Company("Health Plus", CompanyIndustryType.IT, LocalDateTime.now(), LocalDateTime.now());
            Company retailGlobal = new Company("Retail Global", CompanyIndustryType.MARKETING, LocalDateTime.now(), LocalDateTime.now());
            companyRepository.saveAll(Arrays.asList(techCorp, financeInc, marketingPro, healthPlus, retailGlobal));

            // Create Customers
            Customer john = new Customer(techCorp, "John Doe", "john@techcorp.com", "123-456-7890", LocalDateTime.now(), LocalDateTime.now());
            Customer jane = new Customer(techCorp, "Jane Smith", "jane@techcorp.com", "098-765-4321", LocalDateTime.now(), LocalDateTime.now());
            Customer bob = new Customer(financeInc, "Bob Wilson", "bob@financeinc.com", "555-0123", LocalDateTime.now(), LocalDateTime.now());
            Customer alice = new Customer(marketingPro, "Alice Brown", "alice@marketingpro.com", "555-4567", LocalDateTime.now(), LocalDateTime.now());
            Customer charlie = new Customer(healthPlus, "Charlie Davis", "charlie@healthplus.com", "555-8888", LocalDateTime.now(), LocalDateTime.now());
            Customer diana = new Customer(retailGlobal, "Diana Prince", "diana@retailglobal.com", "555-9999", LocalDateTime.now(), LocalDateTime.now());
            Customer eve = new Customer(techCorp, "Eve Online", "eve@techcorp.com", "555-1111", LocalDateTime.now(), LocalDateTime.now());
            Customer frank = new Customer(financeInc, "Frank Castle", "frank@financeinc.com", "555-2222", LocalDateTime.now(), LocalDateTime.now());
            Customer grace = new Customer(marketingPro, "Grace Hopper", "grace@marketingpro.com", "555-3333", LocalDateTime.now(), LocalDateTime.now());
            Customer hank = new Customer(healthPlus, "Hank Pym", "hank@healthplus.com", "555-4444", LocalDateTime.now(), LocalDateTime.now());
            
            customerRepository.saveAll(Arrays.asList(john, jane, bob, alice, charlie, diana, eve, frank, grace, hank));

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

            Ticket t5 = new Ticket("API Integration Issue", "Unable to connect to the third-party API", TicketPriority.HIGH, TicketStatus.OPEN, LocalDateTime.now(), LocalDateTime.now());
            t5.setCompany(healthPlus);
            t5.setCustomer(charlie);
            t5.setAssignedUser(user3);

            Ticket t6 = new Ticket("Website Slowdown", "The homepage takes too long to load", TicketPriority.MEDIUM, TicketStatus.IN_PROGRESS, LocalDateTime.now(), LocalDateTime.now());
            t6.setCompany(retailGlobal);
            t6.setCustomer(diana);
            t6.setAssignedUser(user4);

            Ticket t7 = new Ticket("New Account Setup", "Help needed with setting up a new account", TicketPriority.LOW, TicketStatus.OPEN, LocalDateTime.now(), LocalDateTime.now());
            t7.setCompany(techCorp);
            t7.setCustomer(eve);
            t7.setAssignedUser(user1);

            Ticket t8 = new Ticket("Payment Failure", "Credit card payment was declined", TicketPriority.HIGH, TicketStatus.OPEN, LocalDateTime.now(), LocalDateTime.now());
            t8.setCompany(financeInc);
            t8.setCustomer(frank);
            t8.setAssignedUser(user2);

            Ticket t9 = new Ticket("Logo Update", "Request to update the company logo on the portal", TicketPriority.LOW, TicketStatus.IN_PROGRESS, LocalDateTime.now(), LocalDateTime.now());
            t9.setCompany(marketingPro);
            t9.setCustomer(grace);
            t9.setAssignedUser(user3);

            Ticket t10 = new Ticket("Security Vulnerability", "Report of a potential security flaw", TicketPriority.HIGH, TicketStatus.OPEN, LocalDateTime.now(), LocalDateTime.now());
            t10.setCompany(healthPlus);
            t10.setCustomer(hank);
            t10.setAssignedUser(admin);

            Ticket t11 = new Ticket("Data Export Request", "Requesting a full export of customer data", TicketPriority.MEDIUM, TicketStatus.CLOSED, LocalDateTime.now(), LocalDateTime.now());
            t11.setCompany(techCorp);
            t11.setCustomer(john);
            t11.setAssignedUser(user4);

            Ticket t12 = new Ticket("Broken Link", "The support link in the footer is broken", TicketPriority.LOW, TicketStatus.CLOSED, LocalDateTime.now(), LocalDateTime.now());
            t12.setCompany(marketingPro);
            t12.setCustomer(alice);
            t12.setAssignedUser(user1);

            ticketRepository.saveAll(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));

            // Create Customer Notes
            CustomerNote n1 = new CustomerNote(john, user1, "Called customer regarding downtime, they are very frustrated.", LocalDateTime.now(), LocalDateTime.now());
            CustomerNote n2 = new CustomerNote(bob, user2, "Sent invoice details to customer.", LocalDateTime.now(), LocalDateTime.now());
            CustomerNote n3 = new CustomerNote(alice, admin, "Customer is interested in premium plan.", LocalDateTime.now(), LocalDateTime.now());
            CustomerNote n4 = new CustomerNote(charlie, user3, "Customer reported API issues starting this morning.", LocalDateTime.now(), LocalDateTime.now());
            CustomerNote n5 = new CustomerNote(diana, user4, "Investigating website performance issues.", LocalDateTime.now(), LocalDateTime.now());
            CustomerNote n6 = new CustomerNote(frank, user2, "Discussed payment options with customer.", LocalDateTime.now(), LocalDateTime.now());
            CustomerNote n7 = new CustomerNote(hank, admin, "Customer provided details about the security vulnerability.", LocalDateTime.now(), LocalDateTime.now());
            
            customerNoteRepository.saveAll(Arrays.asList(n1, n2, n3, n4, n5, n6, n7));

            System.out.println("Test data initialized successfully.");
        };
    }
}
