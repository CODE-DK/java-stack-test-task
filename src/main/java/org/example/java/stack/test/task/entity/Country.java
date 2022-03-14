package org.example.java.stack.test.task.entity;

import lombok.Value;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigInteger;

@Value
@Table("countries")
public class Country {
    @Column("country_code")
    String countryCode;
    BigInteger counter;
}
