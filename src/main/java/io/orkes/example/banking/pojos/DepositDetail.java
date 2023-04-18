package io.orkes.example.banking.pojos;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DepositDetail {

    private String accountId;
    private BigDecimal amount;

}
