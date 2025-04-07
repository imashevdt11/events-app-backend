package kg.something.events_app_backend.service.impl;

import kg.something.events_app_backend.entity.Booking;
import kg.something.events_app_backend.mapper.BookingRepository;
import kg.something.events_app_backend.service.BookingService;
import org.springframework.stereotype.Service;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository repository;

    public BookingServiceImpl(BookingRepository repository) {
        this.repository = repository;
    }

    public void save(Booking booking) {
        repository.save(booking);
    }
}
