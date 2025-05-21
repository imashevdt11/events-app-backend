package kg.something.events_app_backend.repository;

import kg.something.events_app_backend.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ComplaintRepository extends JpaRepository<Complaint, UUID> {
    Complaint findComplaintById(UUID id);
}
