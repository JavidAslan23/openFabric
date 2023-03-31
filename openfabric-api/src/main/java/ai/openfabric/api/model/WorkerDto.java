package ai.openfabric.api.model;

import lombok.Data;

/**
 * @author Cavid Aslanov
 * @time 31/03/2023 - 4:58 PM
 **/
@Data
public class WorkerDto {
    private String id;

    private String name;
    private Integer port;
    private String image;
    private String status;
}
