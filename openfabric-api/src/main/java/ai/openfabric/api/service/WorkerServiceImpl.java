package ai.openfabric.api.service;

import ai.openfabric.api.model.Worker;
import ai.openfabric.api.model.WorkerDto;
import ai.openfabric.api.model.WorkerStatistics;
import ai.openfabric.api.repository.WorkerRepository;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Container;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Cavid Aslanov
 * @time 30/03/2023 - 3:15 PM
 **/
@Service
public class WorkerServiceImpl implements WorkerService {

    @Autowired
    private DockerClient dockerClient;

    @Autowired
    private WorkerRepository workerRepository;

    @Override
    public List<WorkerDto> listWorkersPaginated(int pageNo,int pageSize) {

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Worker> workers = workerRepository.findAll(pageable);

        List<Worker> workersList = workers.getContent();
        return workersList.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public void startWorker(String containerId) {

        dockerClient.startContainerCmd(containerId).exec();

    }

    @Override
    public void stopWorker(String containerId) {

        dockerClient.stopContainerCmd(containerId).exec();
    }

    @Override
    public Worker getWorkerInfo(String id) {

        return workerRepository.findById(id).get();

    }

    @Override
    public WorkerStatistics getWorkerStatistics(String containerId) {

        WorkerStatistics workerStatistics = new WorkerStatistics();

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(containerId).exec();

        workerStatistics.setId(inspectContainerResponse.getId());
        workerStatistics.setName(inspectContainerResponse.getName());
        workerStatistics.setImageId(inspectContainerResponse.getImageId());
        workerStatistics.setLogPath(inspectContainerResponse.getLogPath());
        workerStatistics.setCreated(inspectContainerResponse.getCreated());
        workerStatistics.setDriver(inspectContainerResponse.getDriver());


        return workerStatistics;
    }

    @Override
    @Transactional
    public String createWorker() {

        String imageId = dockerClient.buildImageCmd()
                .withDockerfile(new File("C:\\Users\\CavidAslan\\Desktop\\openfabric-test\\openfabric-api\\dockerfile"))
//                .withPull(true)
//                .withNoCache(true)
                .withTag("openFabric")
                .exec(new BuildImageResultCallback())
                .awaitImageId();


        CreateContainerResponse createContainerResponse = (CreateContainerResponse) dockerClient.createContainerCmd(imageId);
        dockerClient.startContainerCmd(createContainerResponse.getId());

        List<Container> containers = dockerClient.listContainersCmd().exec();
        Container containerOfWorker = containers.stream().filter(container -> container.getImageId().equals(imageId)).findFirst().get();

        Worker worker = new Worker();
        worker.setId(containerOfWorker.getId());
        worker.setName(containerOfWorker.getNames().toString());
        worker.setPort(Arrays.stream(containerOfWorker.getPorts()).findFirst().get().getPublicPort());
        worker.setImage(containerOfWorker.getImageId());
        workerRepository.save(worker);

        return "worker container created with id : " + createContainerResponse.getId();
    }


    private WorkerDto mapToDTO(Worker worker){
        WorkerDto dto = new WorkerDto();
        dto.setId(worker.getId());
        dto.setName(worker.getName());
        dto.setPort(worker.getPort());
        dto.setImage(worker.getImage());

        return dto;
    }
}
