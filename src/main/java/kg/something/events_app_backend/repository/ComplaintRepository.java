package kg.something.events_app_backend.repository;

import kg.something.events_app_backend.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, UUID> {

    Complaint findComplaintById(UUID id);
}
