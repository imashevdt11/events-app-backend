package kg.something.events_app_backend.dto;

public final class EndpointConstants {

    private static final String API_PREFIX = "/api";

    public static final String APP_USER_ENDPOINT = API_PREFIX + "/app-user";

    public static final String AUTH_ENDPOINT = API_PREFIX + "/auth";

    public static final String[] WHITE_LIST_URL = {
            "/api/users/sign-up",
            "/api/users/login",
            "/api/roles",
            AUTH_ENDPOINT.concat("/confirm-email"),
            AUTH_ENDPOINT.concat("/login"),
            AUTH_ENDPOINT.concat("/login-admin"),
            AUTH_ENDPOINT.concat("/refresh-token"),
            AUTH_ENDPOINT.concat("/resend-confirmation-code"),
            AUTH_ENDPOINT.concat("/sign-up"),
            APP_USER_ENDPOINT.concat("/confirm-subscription-request**"),
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/websocket/**",
            "/app/**",
            "/topic/**",
            "/notification/push"
    };
}