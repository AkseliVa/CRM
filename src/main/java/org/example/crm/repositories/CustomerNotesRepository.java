package org.example.crm.repositories;

import org.example.crm.entities.CustomerNotes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerNotesRepository extends JpaRepository<CustomerNotes, Long> {
}
