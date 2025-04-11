package kg.something.events_app_backend.repository;

import kg.something.events_app_backend.entity.Subscription;
import kg.something.events_app_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    Subscription findSubscribeByOrganizerAndSubscriber(User organizer, User subscriber);

    List<Subscription> findAllBySubscriber(User subscriber);

    List<Subscription> findAllByOrganizer(User organizer);
}
