package io.orkes.example.banking.workers;

import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PollUntilConditionMeetsWorker implements Worker {

    public static final String CURRENT_ITERATION = "currentIteration";
    public static final String POLL_COUNTER = "pollCounter";
    public static final String POLL_INTERVAL_SECONDS = "pollIntervalSeconds";
    int defaultPollCount = 3;

    @Override
    public String getTaskDefName() {
        return "poll-until-condition-matches-full-worker";
    }

    // docs-marker-start-1
    @Override
    public TaskResult execute(Task task) {
        TaskResult taskResult = new TaskResult(task);
        if (!task.getInputData().containsKey(POLL_COUNTER)) {
            taskResult.addOutputData("message", "pollCounter param not found in input, will use default of " + defaultPollCount + " polls");
        }

        int pollCounter = Math.min(10, castToInt(task.getInputData().getOrDefault(POLL_COUNTER, defaultPollCount)));
        int pollIntervalSeconds = Math.min(10, castToInt(task.getInputData().getOrDefault(POLL_INTERVAL_SECONDS, 5)));

        // Add these to the output for context
        taskResult.addOutputData(POLL_INTERVAL_SECONDS, pollIntervalSeconds + " (this test task has a max limit of 10 seconds)");
        taskResult.addOutputData(POLL_COUNTER, pollCounter + " (this test task has a max limit of 10 iterations)");

        // We can read current iteration from the task output as the data will be retained on the worker when polled
        int currentIteration = castToInt(taskResult.getOutputData().getOrDefault(CURRENT_ITERATION, 0));

        // Increment the current iteration and set to the task output
        taskResult.addOutputData(CURRENT_ITERATION, ++currentIteration);
        taskResult.addOutputData("updatedTime", new Date().toString());

        // While condition is not met, keep task in progress
        if (currentIteration < pollCounter) {
            taskResult.setStatus(TaskResult.Status.IN_PROGRESS);
            // Set to configured seconds to callback, and you can set this to any value as per the requirements
            taskResult.setCallbackAfterSeconds(pollIntervalSeconds);
            return taskResult;
        }

        // Set task as completed now that the poll count condition is met
        taskResult.setStatus(TaskResult.Status.COMPLETED);
        return taskResult;
    }
    // docs-marker-end-1

    public static Integer castToInt(Object obj) {
        if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }
        return Integer.parseInt(obj.toString());
    }

}


