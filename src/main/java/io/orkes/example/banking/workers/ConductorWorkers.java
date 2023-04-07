package io.orkes.example.banking.workers;

import com.netflix.conductor.sdk.workflow.task.WorkerTask;
import io.orkes.example.banking.pojos.DepositDetail;
import io.orkes.example.banking.pojos.FraudCheckResult;
import io.orkes.example.banking.service.FraudCheckService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ConductorWorkers {

    private final FraudCheckService fraudCheckService;

    // docs-marker-start-1

    /** Note: Using this setting, up to 5 tasks will run in parallel, with tasks being polled every 200ms */
    @WorkerTask(value = "fraud-check", threadCount = 5, pollingInterval = 200)
    public FraudCheckResult checkForFraudTask(DepositDetail depositDetail) {
        return fraudCheckService.checkForFraud(depositDetail);
    }

    // docs-marker-end-1

}
