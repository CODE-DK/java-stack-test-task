package org.example.java.stack.test.task.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountryDTO {
    private String countryCode;
    private String counterValue;
}
