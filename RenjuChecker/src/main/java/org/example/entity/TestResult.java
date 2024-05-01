package org.example.entity;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TestResult {
    private int winner;
    private int horizontalPos;
    private int verticalPos;

}
