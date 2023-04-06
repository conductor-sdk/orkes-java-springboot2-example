package io.orkes.example.banking.controller;

import io.orkes.example.banking.pojos.DepositDetail;
import io.orkes.example.banking.pojos.FraudCheckResult;
import io.orkes.example.banking.service.FraudCheckService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
public class BankingApiController {

    private final FraudCheckService fraudCheckService;

    @PostMapping(value = "/checkForFraud", produces = "application/json")
    public ResponseEntity<FraudCheckResult> checkForFraud(@RequestBody DepositDetail depositDetail) {
        log.info("Checking for fraud: {}", depositDetail);
        return ResponseEntity.ok(fraudCheckService.checkForFraud(depositDetail));
    }

}
