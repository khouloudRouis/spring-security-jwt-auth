package com.khouloud.auth.api.exception;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(int status, String message, Instant timestamp, String path, Map<String, String> errors) {
}
