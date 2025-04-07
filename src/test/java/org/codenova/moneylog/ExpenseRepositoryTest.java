package org.codenova.moneylog;

import org.codenova.moneylog.query.DailyExpense;
import org.codenova.moneylog.repository.ExpenseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ExpenseRepositoryTest {

    @Autowired
    ExpenseRepository expenseRepository;
    @Test
    public void dailyExpenseTest() {
        List<DailyExpense> list = expenseRepository.getDailyExpenseByUserIdAndPeriod(2,
                LocalDate.of(2025, 4, 1),
                LocalDate.of(2025,4,30));
        for (DailyExpense expense : list) {
           // System.out.println(expense);
        }
        System.out.println("=================================================================================");
      //  list.add(4,
        //        DailyExpense.builder().expenseDate(LocalDate.of(2025,4,1)).total(0).build());

        for (DailyExpense expense : list) {
           // System.out.println(expense);
        }
    }

    @Test
    public void test2() {
        LocalDate from = LocalDate.of(2025, 4, 1);
        LocalDate to = LocalDate.of(2025, 4, 30);

        // q반복문 이용해서 모든 날짜 로컬데이트로출력

        List<LocalDate> dates = new ArrayList<>();

        dates.add(from.plusDays(0));
        dates.add(from.plusDays(1));
       // System.out.println(dates);

        for (int i = 0; from.plusDays(i).isBefore(to) || from.plusDays(i).isEqual(to); i++) {
            dates.add(from.plusDays(i));
        }

        System.out.println(dates);
        List<DailyExpense> expenses = new ArrayList<>();
        for (LocalDate date : dates) {
            expenses.add(DailyExpense.builder().expenseDate(date).total(0).build()
            );
        }
        //======================================================================================
        for (DailyExpense one : expenses) {
            //System.out.println(one);
        }
    }
    @Test
    public void test3() {
        LocalDate from = LocalDate.of(2025, 4, 1);
        LocalDate to = LocalDate.of(2025, 4, 14);

        List<DailyExpense> list = expenseRepository.getDailyExpenseByUserIdAndPeriod(2, from, to);
        Map<LocalDate, DailyExpense> dateMap = new HashMap<>();
        for (DailyExpense expense : list) {
            dateMap.put(expense.getExpenseDate(), expense);
        }
        System.out.println(dateMap);
        System.out.println("======================================================================================");
        List<DailyExpense> fullList = new ArrayList<>();
        for (int i =0 ; from.plusDays(i).isBefore(to) || from.plusDays(i).isEqual(to); i++) {
            LocalDate d = from.plusDays(i);
            //if 만약 이 날짜에 해당하는 데이터를 불러왔다면, 그때는 그걸 add
            if (dateMap.get(d) != null) {
                fullList.add(dateMap.get(d));
            }else {
                //else ㅇ없다면 이 날짜로 데이터를 생성해서 add
                fullList.add(DailyExpense.builder().expenseDate(d).total(0).build());
            }
        }
        System.out.println("=================================================================================");
        for (DailyExpense expense : fullList) {
            System.out.println("이건가?? " + expense);
        }
    }
    //ㅈㄴ모르겟는디 ??????????????????????????????????????????????????????????????
}
