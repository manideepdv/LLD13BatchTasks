package com.example.qcommerce.services;

import com.example.qcommerce.adapters.GmapsAdapter;
import com.example.qcommerce.adapters.MapsAdapter;
import com.example.qcommerce.exceptions.BatchedTaskNotFoundException;
import com.example.qcommerce.models.BatchedTask;
import com.example.qcommerce.models.Location;
import com.example.qcommerce.models.Task;
import com.example.qcommerce.repositories.BatchedTaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BatchedTaskServiceImpl implements BatchedTaskService {
    private final BatchedTaskRepository batchedTaskRepository;
    private final MapsAdapter mapsAdapter;

    public BatchedTaskServiceImpl(BatchedTaskRepository batchedTaskRepository, GmapsAdapter gmapsAdapter) {
        this.batchedTaskRepository = batchedTaskRepository;
        this.mapsAdapter = gmapsAdapter;
    }

    @Override
    public List<Location> buildRoute(long batchedTaskId) throws BatchedTaskNotFoundException {
        Optional<BatchedTask> optionalBatchedTask = batchedTaskRepository.findById(batchedTaskId);
        if (optionalBatchedTask.isEmpty()) {
            throw new BatchedTaskNotFoundException("Batched task not found");
        }
        BatchedTask batchedTask = optionalBatchedTask.get();
        List<Location> dropLocations = batchedTask.getTasks().stream().map(Task::getDropLocation).toList();
        return mapsAdapter.buildRoute(dropLocations);
    }
}
