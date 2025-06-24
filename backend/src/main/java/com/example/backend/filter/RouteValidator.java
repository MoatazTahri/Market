package com.example.backend.filter;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Component
public class RouteValidator {

    public static final List<String> openRoutes = List.of(
            "/auth",
            "/uploads/products",
            "/user/check-email",
            "/product/[0-9]+$", // Means product/{id}
            "/product/all"
    );

    // Functional interface to filter non-open urls (returns true if not exists in the list)
    public Predicate<HttpServletRequest> isAuthNeeded = request ->
            openRoutes.stream()
                    .noneMatch(pattern -> Pattern.compile(pattern).matcher(request.getRequestURI()).find());

}
