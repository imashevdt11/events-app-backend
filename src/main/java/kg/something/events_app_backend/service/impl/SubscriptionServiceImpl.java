package kg.something.events_app_backend.service.impl;

import kg.something.events_app_backend.entity.Subscription;
import kg.something.events_app_backend.entity.User;
import kg.something.events_app_backend.repository.SubscriptionRepository;
import kg.something.events_app_backend.service.SubscriptionService;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository repository;

    public SubscriptionServiceImpl(SubscriptionRepository repository) {
        this.repository = repository;
    }

    public Subscription findSubscribeByOrganizerAndSubscriber(User organizer, User user) {
        return repository.findSubscribeByOrganizerAndSubscriber(organizer, user);
    }

    public void save(Subscription subscription) {
        repository.save(subscription);
    }

    public void delete(Subscription subscription) {
        repository.delete(subscription);
    }
}
