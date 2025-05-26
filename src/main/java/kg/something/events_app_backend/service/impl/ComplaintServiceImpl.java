package kg.something.events_app_backend.service.impl;

import kg.something.events_app_backend.dto.response.ComplaintListResponse;
import kg.something.events_app_backend.entity.Complaint;
import kg.something.events_app_backend.entity.Event;
import kg.something.events_app_backend.entity.User;
import kg.something.events_app_backend.enums.ComplaintStatus;
import kg.something.events_app_backend.exception.InvalidRequestException;
import kg.something.events_app_backend.exception.ResourceNotFoundException;
import kg.something.events_app_backend.mapper.ComplaintMapper;
import kg.something.events_app_backend.repository.ComplaintRepository;
import kg.something.events_app_backend.service.ComplaintService;
import kg.something.events_app_backend.service.EventService;
import kg.something.events_app_backend.service.UserService;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository repository;
    private final UserService userService;
    private final EventService eventService;
    private final ComplaintMapper complaintMapper;

    public ComplaintServiceImpl(ComplaintRepository repository, UserService userService, EventService eventService, ComplaintMapper complaintMapper) {
        this.repository = repository;
        this.userService = userService;
        this.eventService = eventService;
        this.complaintMapper = complaintMapper;
    }

    @Override
    public List<ComplaintListResponse> getAllComplaints() {
        User user = userService.getAuthenticatedUser();
        if (!user.getRole().getName().equals("ROLE_ADMIN")) {
            throw new InvalidRequestException("Только администраторы могут просматривать жалобы");
        }
        return repository.findAll().stream()
                .map(complaintMapper::toComplaintListResponse)
                .toList();
    }

    @Override
    public List<String> getAllComplaintsStatuses() {
        User user = userService.getAuthenticatedUser();
        if (!user.getRole().getName().equals("ROLE_ADMIN")) {
            throw new InvalidRequestException("Только администраторы могут просматривать жалобы");
        }
        return Arrays.stream(ComplaintStatus.class.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public ComplaintListResponse getComplaintById(UUID id) {
        Complaint complaint = repository.findComplaintById(id);
        if (complaint == null) {
            throw new ResourceNotFoundException("Жалоба с id '%s' не найдена".formatted(id));
        }
        return complaintMapper.toComplaintListResponse(complaint);
    }

    @Override
    public String changeComplaintStatus(UUID complaintId, String status) {
        User user = userService.getAuthenticatedUser();
        Complaint complaint = findComplaintById(complaintId);
        String previousStatus = complaint.getComplaintStatus().name();

        if (!user.getRole().getName().equals("ROLE_ADMIN")) {
            throw new InvalidRequestException("Только администраторы могут изменять статус жалобы");
        }
        if (!EnumUtils.isValidEnum(ComplaintStatus.class, status)) {
            throw new ResourceNotFoundException("Названия статуса жалобы '%s' нет в базе данных".formatted(status));
        }
        if (previousStatus.equals(status)) {
            throw new InvalidRequestException("Переданный и нынешний статус совпадают");
        }

        complaint.setComplaintStatus(ComplaintStatus.valueOf(status));
        repository.save(complaint);

        return "Статус жалобы изменен с '%s' на '%s'".formatted(previousStatus, status);
    }

    public Complaint findComplaintById(UUID id) {
        return Optional.ofNullable(repository.findComplaintById(id))
                .orElseThrow(() -> new ResourceNotFoundException("Жалоба с id '%s' не найдена в базе данных".formatted(id)));
    }

    @Override
    public String sendComplaint(UUID eventId, String text) {
        User user = userService.getAuthenticatedUser();
        Event event = eventService.findEventById(eventId);

        if (event.getOrganizerUser().getId().equals(user.getId())) {
            throw new InvalidRequestException("Пользователь не может отправить жалобу на самого себя");
        }
        repository.save(new Complaint(text, user, event));
        return "Жалоба отправлена на рассмотрение";
    }
}
