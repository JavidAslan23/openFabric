package ai.openfabric.api.controller;

import ai.openfabric.api.model.Worker;
import ai.openfabric.api.model.WorkerDto;
import ai.openfabric.api.model.WorkerStatistics;
import ai.openfabric.api.service.WorkerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("${node.api.path}/worker")
public class WorkerController {

    private final Logger logger = LoggerFactory.getLogger(WorkerController.class);

    @Autowired
    private WorkerService workerService;

    @PostMapping(path = "/hello")
    public @ResponseBody String hello(@RequestBody String name) {
        return "Hello!" + name;
    }

    @GetMapping(path = "/listWorkers")
    public List<WorkerDto> listWorkersPaginated(@RequestParam(name = "page", defaultValue = "0") int page,
                                                @RequestParam(name = "size", defaultValue = "15") int size) {

        logger.info("list workers...");
        return workerService.listWorkersPaginated(page,size);
    }

    @PostMapping(path = "/startWorker")
    public void startWorker(@RequestParam String containerId) {

        logger.info("starting worker..");
        workerService.startWorker(containerId);
    }

    @GetMapping(path = "/stopWorker")
    public void stopWorker(@RequestParam String containerId) {

        logger.info("stopping worker...");
        workerService.stopWorker(containerId);
    }

    @GetMapping(path = "/workerInfo")
    public Worker getWorkerInfo(@RequestParam String workerId) {

        logger.info("worker info getting...");
        return workerService.getWorkerInfo(workerId);
    }

    @GetMapping(path = "/workerStatistics")
    public WorkerStatistics getWorkerStatistics(@RequestParam String containerId) {

        logger.info("worker statistics getting...");
        return workerService.getWorkerStatistics(containerId);
    }

    @PostMapping(path = "/createWorker")
    public String createWorker() {

        logger.info("creating worker..");
        return workerService.createWorker();
    }

}
