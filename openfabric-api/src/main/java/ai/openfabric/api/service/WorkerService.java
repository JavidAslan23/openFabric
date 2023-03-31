package ai.openfabric.api.service;

import ai.openfabric.api.model.Worker;
import ai.openfabric.api.model.WorkerDto;
import ai.openfabric.api.model.WorkerStatistics;

import java.util.List;

/**
 * @author Cavid Aslanov
 * @time 30/03/2023 - 3:16 PM
 **/
public interface WorkerService {

    List<WorkerDto> listWorkersPaginated(int pageNo, int pageSize);

    void startWorker(String containerId);

    void stopWorker(String containerId);

    Worker getWorkerInfo(String id);

    WorkerStatistics getWorkerStatistics(String containerId);

    String  createWorker();


}
