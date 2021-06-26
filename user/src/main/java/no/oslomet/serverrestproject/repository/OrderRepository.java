package no.oslomet.serverrestproject.repository;

import no.oslomet.serverrestproject.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
