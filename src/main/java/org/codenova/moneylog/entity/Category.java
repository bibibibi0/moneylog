package org.codenova.moneylog.entity;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private int id;
    private int sort;
    private String name;
}
