package ai.openfabric.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Cavid Aslanov
 * @time 31/03/2023 - 2:44 AM
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkerStatistics {

    private String id;
    private String name;
    private String logPath;
    private String imageId;
    private String created;
    private String driver;
}
