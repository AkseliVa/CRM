package org.example.crm.repositories;

import org.example.crm.entities.CustomerNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerNoteRepository extends JpaRepository<CustomerNote, Long> {
}
