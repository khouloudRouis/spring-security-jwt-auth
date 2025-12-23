package com.khouloud.auth.api.advice;

import java.util.Map;

public record ErrorResponse (int status, String Message, Map<String, String> errors) {}
