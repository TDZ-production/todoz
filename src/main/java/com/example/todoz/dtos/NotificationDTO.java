package com.example.todoz.dtos;

import java.util.List;

public record NotificationDTO(String title, List<String> tasks) {
}
