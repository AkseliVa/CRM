package org.example.crm.repositories;

import org.example.crm.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT t FROM Ticket t " +
            "LEFT JOIN FETCH t.company " +
            "LEFT JOIN FETCH t.customer " +
            "LEFT JOIN FETCH t.assignedUser")
    List<Ticket> findAllWithRelations();
}
