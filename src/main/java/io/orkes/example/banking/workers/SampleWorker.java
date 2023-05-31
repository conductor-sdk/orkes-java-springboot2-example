package io.orkes.example.banking.workers;

import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;
import org.springframework.stereotype.Component;

@Component
public class SampleWorker implements Worker {

    @Override
    public String getTaskDefName() {
        return "sample-worker-full-implementation";
    }

    @Override
    public TaskResult execute(Task task) {
        return TaskResult.newTaskResult(TaskResult.Status.COMPLETED).addOutputData("sample-worker-full-implementation", "successful");
    }

}
