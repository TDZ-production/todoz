package com.example.todoz.controllers;

import com.example.todoz.feedback.Feedback;
import com.example.todoz.feedback.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService service;


    @GetMapping("/feedback")
    public String showFeedback() {
        return "feedback";
    }

    @PostMapping("/feedback")
    public String saveFeedback(RedirectAttributes ra, Feedback feedback) {
        service.save(feedback);
        ra.addFlashAttribute("received", true);
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