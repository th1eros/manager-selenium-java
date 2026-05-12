package com.malebolge.sandbox.auth;

import java.util.List;
import java.util.Map;

public class AuthorityChecker {
    private static final Map<String, List<String>> ROLE_PERMISSIONS = Map.of(
            "ADMIN", List.of("ALL"),
            "DEVELOPER", List.of("READ", "WRITE"),
            "AUDITOR", List.of("READ")
    );
    public static boolean hasAccess(String role, String permission) {
        if ("ADMIN".equals(role)) {
            return true; 
        }
        List<String> perms = ROLE_PERMISSIONS.getOrDefault(role,List.of());
        return perms.contains(permission);
    }
}
