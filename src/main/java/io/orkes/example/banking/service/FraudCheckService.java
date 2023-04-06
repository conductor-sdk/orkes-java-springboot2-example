package io.orkes.example.banking.service;

import io.orkes.example.banking.pojos.DepositDetail;
import io.orkes.example.banking.pojos.FraudCheckResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static io.orkes.example.banking.pojos.FraudCheckResult.Result.FAIL;
import static io.orkes.example.banking.pojos.FraudCheckResult.Result.PASS;

@Service
public class FraudCheckService {

    public FraudCheckResult checkForFraud(DepositDetail depositDetail) {
        FraudCheckResult fcr = new FraudCheckResult();
        if(depositDetail.getAmount().compareTo(BigDecimal.valueOf(100000)) > 0) {
            fcr.setResult(FAIL);
            fcr.setReason("Amount too large");
        } else {
            fcr.setResult(PASS);
            fcr.setReason("All good!");
        }
        return fcr;
    }

}
