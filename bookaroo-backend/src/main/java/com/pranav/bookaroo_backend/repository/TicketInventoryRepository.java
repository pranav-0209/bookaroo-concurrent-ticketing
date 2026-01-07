package com.pranav.bookaroo_backend.repository;

import com.pranav.bookaroo_backend.model.TicketInventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketInventoryRepository extends JpaRepository<TicketInventory,Long> {

    Optional<TicketInventory> findByEventId(Long eventId);

}
