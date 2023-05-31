package io.orkes.example.banking.workers;

import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;
import org.springframework.stereotype.Component;

@Component
public class PollUntilConditionMeetsWorker implements Worker {

    public static final String CURRENT_ITERATION = "currentIteration";
    public static final String POLL_COUNTER = "pollCounter";
    int defaultPollCount = 3;

    @Override
    public String getTaskDefName() {
        return "poll-until-condition-matches-full-worker";
    }

    // docs-marker-start-1
    @Override
    public TaskResult execute(Task task) {
        TaskResult taskResult = new TaskResult(task);
        if(!task.getInputData().containsKey(POLL_COUNTER)) {
            taskResult.addOutputData("message", "pollCounter param not found in input, will use default of " + defaultPollCount + " polls");
        }
        int pollCounter = ((Number) task.getInputData().getOrDefault(POLL_COUNTER, defaultPollCount)).intValue();
        if (pollCounter > 10) {
            taskResult.addOutputData("message", "for this test worker, max poll count limit is 10");
            pollCounter = 10;
        }
        taskResult.addOutputData(POLL_COUNTER, pollCounter);

        // The task output data will be retained on the worker when polled
        int currentIteration = ((Number) taskResult.getOutputData().getOrDefault(CURRENT_ITERATION, 0)).intValue();
        taskResult.addOutputData(CURRENT_ITERATION, currentIteration);

        // While condition is not met, keep task in progress
        if(currentIteration < pollCounter) {
            taskResult.setStatus(TaskResult.Status.IN_PROGRESS);
            // Set to 10 seconds to callback, but you can set this to any value as per the requirements
            taskResult.setCallbackAfterSeconds(10);
            return taskResult;
        }

        // Set task as completed now that the poll count condition is met
        taskResult.setStatus(TaskResult.Status.COMPLETED);
        return taskResult;
    }
    // docs-marker-end-1

}
