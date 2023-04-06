package io.orkes.example.banking.pojos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FraudCheckResult {

    public enum Result {
        PASS, FAIL;
    }

    private Result result;
    private String reason;

}
