package org.codenova.moneylog;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootTest
class MoneylogApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void compareLocalDatetime() {
        LocalDateTime t1 = LocalDateTime.of(2025, 1, 21, 1, 30);
        LocalDateTime t2 = LocalDateTime.of(2025, 1, 20, 13, 00);
        LocalDateTime t3 = LocalDateTime.of(2025, 2, 20, 00, 00);

        System.out.println(t1.isBefore(t2)); // t
        System.out.println(t3.isAfter(t2));  // t
        System.out.println(t1.isAfter(t3)); // f
    }

    @Test
    void uuidTest() {
        String uuid = UUID.randomUUID().toString();
        System.out.println(uuid);
        System.out.println(uuid.replace("-",""));
    }
    /*
    LocalDatetime 크기비교 (과거,미래 비교) 할 때 isAfter, isBefore ==> boolean
    t1.isAfter(t2) >> t1이 t2 이후인가?
    t2.isBefore(t3) >> t2가 t3 이전인가?
     */
}
