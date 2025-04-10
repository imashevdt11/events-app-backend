package kg.something.events_app_backend.service;

import kg.something.events_app_backend.entity.Subscription;
import kg.something.events_app_backend.entity.User;

public interface SubscriptionService {

    Subscription findSubscribeByOrganizerAndSubscriber(User organizer, User subscriber);

    void save(Subscription subscription);

    void delete(Subscription subscription);
}
