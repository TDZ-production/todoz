package com.example.todoz.controllers;


import com.example.todoz.feedback.Feedback;
import com.example.todoz.feedback.FeedbackService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Controller
public class FeedbackController {
    private final WebClient webClient;
    private final FeedbackService service;
    @Value("${discord.webhook}")
    private URI uri;

    public FeedbackController(WebClient.Builder webClientBuilder, FeedbackService service) {
        this.webClient = webClientBuilder.build();
        this.service = service;
    }

    @GetMapping("/feedback")
    public String showFeedback() {
        return "feedback";
    }

    @PostMapping("/feedback")
    public String saveFeedback(RedirectAttributes ra, Feedback feedback) throws URISyntaxException {
        service.save(feedback);
        ra.addFlashAttribute("received", true);

        webClient.post()
                .uri(uri)
                .header("Content-Type", "application/json")
                .body(BodyInserters.fromValue("{\"content\": \"" + feedback.getDescription() + "\"}"))
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
        return "redirect:/feedback";
    }

    @GetMapping("/feedback/read")
    public String showFeedbackList(Model model) {
        model.addAttribute("feedbacks", service.getFeedbacks());
        return "feedbackRead";
    }

    @PostMapping("/feedback/update")
    public String updateFeedback(@RequestParam(value = "feedbackIds", required = false) List<Long> feedbackIds) {
        if (feedbackIds != null && !feedbackIds.isEmpty()) {
            feedbackIds.stream()
                    .map(service::findById)
                    .peek(f -> f.setResolved(true))
                    .forEach(service::save);
        }
        return "redirect:/feedback/read";
    }
}