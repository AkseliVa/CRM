package org.example.crm.repositories;

import org.example.crm.entities.CustomerNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerNoteRepository extends JpaRepository<CustomerNote, Long> {

    @Query("SELECT n FROM CustomerNote n " +
        "LEFT JOIN FETCH n.customer " +
        "LEFT JOIN FETCH n.user")
    List<CustomerNote> findAllWithRelations();

    @Query("SELECT n FROM CustomerNote n " +
        "LEFT JOIN FETCH n.user " +
        "WHERE n.customer.id = :customerId " +
        "ORDER BY n.createdAt DESC")
    List<CustomerNote> findByCustomerIdWithUser(Long customerId);
}
