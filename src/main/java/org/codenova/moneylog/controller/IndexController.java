package org.codenova.moneylog.controller;

import lombok.Getter;
import org.codenova.moneylog.entity.User;
import org.codenova.moneylog.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.time.LocalDate;
import java.util.Optional;

@Controller
public class IndexController {
    @Autowired
    private ExpenseRepository expenseRepository;

    @GetMapping({"/", "/index"})
    public String indexHandle(@SessionAttribute("user") Optional<User> user, Model model) {
        if (user.isEmpty()) {
            return "index";
        } else {
            return "redirect:/home";
        }
    }
    @GetMapping("/home")
    public String homeHandle(@SessionAttribute("user") Optional<User> user, Model model) {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate endDate = today.plusDays(7 - today.getDayOfWeek().getValue());

        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
            model.addAttribute("totalAmount", expenseRepository.findTotalAmountByUserId(user.get().getId()
            , startDate, endDate));
            model.addAttribute("top3Expense"
                    ,expenseRepository.getTop3WeeklyExpenses(user.get().getId(),startDate,endDate));
            model.addAttribute("categoryExpenses", expenseRepository.findTopWeekfindTopCategoryByUserAndWeek
                    (user.get().getId()
            ,startDate,endDate));

            return "home";
        } else {
            return "redirect:/index";
        }
    }

}