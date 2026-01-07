package com.pranav.bookaroo_backend.repository;

import com.pranav.bookaroo_backend.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {

}
