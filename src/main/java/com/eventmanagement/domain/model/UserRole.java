package com.eventmanagement.domain.model;

public enum UserRole {
    ADMIN, ORGANIZER, ATTENDEE;

    public boolean hasPermission(String permission) {
        return switch (this) {
            case ADMIN -> true;
            case ORGANIZER -> !permission.equals("DELETE_ALL");
            case ATTENDEE -> permission.equals("VIEW") || permission.equals("BOOK");
        };
    }
}
