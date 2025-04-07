package org.codenova.moneylog.controller;

import jakarta.validation.Valid;
import org.codenova.moneylog.Request.AddExpenseRequest;
import org.codenova.moneylog.Request.SearchPeriodRequest;
import org.codenova.moneylog.entity.Category;
import org.codenova.moneylog.entity.Expense;
import org.codenova.moneylog.entity.User;
import org.codenova.moneylog.repository.CategoryRepository;
import org.codenova.moneylog.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/expense")
public class ExpenseController {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ExpenseRepository expenseRepository;


    @GetMapping("/history")
    public String historyFindHandle(@SessionAttribute("user")User user,
                                    @ModelAttribute SearchPeriodRequest searchPeriodRequest,
                                    Model model) {
        LocalDate startDate;
        LocalDate endDate;

        if (searchPeriodRequest.getStartDate() != null && searchPeriodRequest.getEndDate() != null){
            startDate = searchPeriodRequest.getStartDate();
            endDate = searchPeriodRequest.getEndDate();
        }else {
            LocalDate today = LocalDate.now();
            startDate = today.minusDays(today.getDayOfMonth() - 1);
            endDate = startDate.plusMonths(1).minusDays(today.getDayOfMonth());
        }

        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("now", LocalDate.now());
        model.addAttribute("categorys", categoryRepository.findAll());
        model.addAttribute("expenses",
                expenseRepository.findByUserIdAndDuration(user.getId(), startDate, endDate));

        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
       // model.addAttribute("expenses", expenseRepository.findByUserIdAndDuration(user.getId()
       // ,LocalDate.now().minusDays(10), LocalDate.now()));

        return "expense/history";
    }

    @PostMapping("/history")
    public String historyPostHandle(@ModelAttribute @Valid AddExpenseRequest addExpenseRequest,
                                    BindingResult bindingResult,
                                    @SessionAttribute("user") User user,
                                    Model model) {
        if (bindingResult.hasErrors()) {
            return "expense/history-error";
        }
        Expense expense = Expense.builder()
                .userId(user.getId())
                .expenseDate(addExpenseRequest.getExpenseDate())
                .description(addExpenseRequest.getDescription())
                .amount(addExpenseRequest.getAmount())
                .categoryId(addExpenseRequest.getCategoryId())
                .build();
        expenseRepository.save(expense);

        return "redirect:/expense/history";
    }

}
