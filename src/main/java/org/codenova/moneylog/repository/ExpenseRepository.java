package org.codenova.moneylog.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.codenova.moneylog.entity.Expense;
import org.codenova.moneylog.query.DailyExpense;
import org.codenova.moneylog.query.ExpenseWithCategory;
import org.codenova.moneylog.query.FindTopCategoryByUserAndWeek;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Mapper
@Repository
public interface ExpenseRepository {
    public int save(Expense expense);

    public List<Expense> findByUserId(@Param("userId") int userId);

    public List<ExpenseWithCategory> findByUserIdAndDuration(@Param("userId") int userId,
                                                 @Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate);

    public List<ExpenseWithCategory> findWithCategoryByUserId(@Param("userId") int userId);

    public List<Expense> getTop3WeeklyExpenses(@Param("userId") int userId,
                                                           @Param("startDate") LocalDate startDate,
                                                           @Param("endDate") LocalDate endDate);

    public int findTotalAmountByUserId(@Param("userId") int userId,
                                       @Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate);

    public List<FindTopCategoryByUserAndWeek> findTopWeekfindTopCategoryByUserAndWeek(@Param("userId") int userId,
                                                                                      @Param("startDate") LocalDate startDate,
                                                                                      @Param("endDate") LocalDate endDate);
    public List<ExpenseWithCategory> getCategoryExpenseByUserIdOrderByCateId(@Param("userId") int userId,
                                                                             @Param("startDate") LocalDate startDate,
                                                                             @Param("endDate") LocalDate endDate);

    public List<ExpenseWithCategory> getCategoryExpenseByUserIdOrderByCategoryId(@Param("userId") int userId,
                                                                             @Param("startDate") LocalDate startDate,
                                                                             @Param("endDate") LocalDate endDate);
    public List<DailyExpense> getDailyExpenseByUserIdAndPeriod(@Param("userId") int userId,
                                                               @Param("startDate") LocalDate startDate,
                                                               @Param("endDate") LocalDate endDate);

}
