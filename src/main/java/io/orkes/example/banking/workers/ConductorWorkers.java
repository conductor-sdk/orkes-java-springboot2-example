package io.orkes.example.banking.workers;

import com.netflix.conductor.sdk.workflow.task.InputParam;
import com.netflix.conductor.sdk.workflow.task.WorkerTask;
import io.orkes.example.banking.pojos.DepositDetail;
import io.orkes.example.banking.pojos.FraudCheckResult;
import io.orkes.example.banking.service.FraudCheckService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@AllArgsConstructor
@Component
@Slf4j
public class ConductorWorkers {

    private final FraudCheckService fraudCheckService;
    private final Random random = new Random();

    // docs-marker-start-1

    /**
     * Note: Using this setting, up to 5 tasks will run in parallel, with tasks being polled every 200ms
     */
    @WorkerTask(value = "fraud-check", threadCount = 5, pollingInterval = 200)
    public FraudCheckResult checkForFraudTask(DepositDetail depositDetail) {
        return fraudCheckService.checkForFraud(depositDetail);
    }

    // docs-marker-end-1


    // docs-marker-start-2

    @WorkerTask(value = "retrieve-deposit-batch", threadCount = 5, pollingInterval = 200)
    public List<DepositDetail> retrieveDepositBatch(@InputParam("batchCount") Integer batchCount) {
        if (batchCount == null) {
            batchCount = random.nextInt(5, 11);
        }
        batchCount = Math.min(100, batchCount); // Limit to 100 in playground
        List<DepositDetail> depositDetails = IntStream.range(0, batchCount)
                .mapToObj(i -> DepositDetail.builder()
                        .accountId("acc-id-" + i)
                        .amount(BigDecimal.valueOf(i * 1500L)) // Create random amounts
                        .build())
                .toList();
        log.info("Returning {} transactions", depositDetails.size());
        return depositDetails;
    }

    // docs-marker-end-2

    // docs-marker-start-3
    @WorkerTask(value = "some_task_with_domain", domain = "test")
    public String workerWithDomain() {
        return "value to be returned at workflow task output";
    }
    // docs-marker-end-3
}
