package kg.something.events_app_backend.enums;

public enum ComplaintType {
    PROPAGANDA_OF_VIOLENCE_OR_EXTREMISM("Пропаганда насилия или экстремизма"),
    OFFENSIVE_OR_DISCRIMINATORY_CONTENT("Оскорбительный или дискриминационный контент"),
    FAKE_EVENT("Фейковое мероприятие"),
    INAPPROPRIATE_IMAGES_OR_VIDEOS("Неприемлемое изображение"),
    COPYRIGHT_INFRINGEMENT("Нарушение авторских прав"),
    VIOLATION_OF_PLATFORM_RULES("Нарушение правил платформы"),
    FRAUD("Мошенничество");

    private final String description;

    ComplaintType(String description) {
        this.description = description;
    }

    public String getDescription() {
            return description;
    }
}
