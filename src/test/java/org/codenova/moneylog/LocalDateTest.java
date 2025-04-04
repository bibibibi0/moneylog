package org.codenova.moneylog;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.time.LocalDate;

@SpringBootTest
public class LocalDateTest {

    @Test
    public void test01(){
        LocalDate today = LocalDate.now();

        DayOfWeek dow = today.getDayOfWeek();  // 요일
        System.out.println(dow);
        int value = today.getDayOfWeek().getValue();  // 요일마다 붙은 값 (예 : 금요일은 5)
        System.out.println(value);

        System.out.println(today.plusDays(1).getDayOfWeek().getValue());
        System.out.println(today.plusDays(2).getDayOfWeek().getValue());

        System.out.println(today.minusDays(5).getDayOfWeek().getValue()); // 7

        /*
            월~일 (1,2,3,4,5,6,7)
            한 주의 시작을 일요일로 볼건지 월요일로 볼건지는 기준 정하기 나름~
         */

        // 우리에게 특정 LocalDate 가 있다고 가정하고, 그 날짜가 포함된 주의 시작일과 끝일을 구하려면?
        // getDayOfweek().getValue() ... if == 3 === > +3 토 +4 일
        // getDayOfweek().getValue() ... if == 3 === > -2 월 -3 일(그 전주)
        // 한 주의 시작을 월, 끝을 일욜이라고 ㄱㄱ
        // dayOfweek ==> 2 -- > -1, +5
        // dayOfweek ==> 3 -- > -2, +4
        // dayOfweek ==> 4 -- > -3, +3
        // dayOfweek ==> 5 -- > -4, +2
        // dayOfweek ==> 6 -- > -5, +1

        LocalDate d = LocalDate.of(2025, 1, 1);
        LocalDate firstDayOfWeek = d.minusDays(d.getDayOfWeek().getValue()-1);
        LocalDate lastDayWeek = d.plusDays(7 - d.getDayOfWeek().getValue());

        System.out.println(firstDayOfWeek);
        System.out.println(lastDayWeek);
        //넘어려운디?


    }
    @Test
    public void test2(){
        LocalDate today = LocalDate.of(2024,2,21);
        System.out.println(today.getDayOfMonth());
        System.out.println(today.minusDays(today.getDayOfMonth() - 1));
        System.out.println(today.plusMonths(1).minusDays(today.getDayOfMonth()));
    }
}
