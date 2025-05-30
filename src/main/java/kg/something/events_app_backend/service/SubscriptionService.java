package kg.something.events_app_backend.service;

import kg.something.events_app_backend.entity.Subscription;
import kg.something.events_app_backend.entity.User;

import java.util.List;

public interface SubscriptionService {

    void delete(Subscription subscription);

    List<Subscription> findAllOrganizersUserFollows(User user);

    List<Subscription> findAllOrganizersSubscribers(User user);

    Subscription findSubscribeByOrganizerAndSubscriber(User organizer, User subscriber);

    void save(Subscription subscription);
}
