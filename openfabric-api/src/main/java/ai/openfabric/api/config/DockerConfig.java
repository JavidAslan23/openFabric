package ai.openfabric.api.config;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Cavid Aslanov
 * @time 31/03/2023 - 4:38 AM
 **/

@Configuration
public class DockerConfig {

//    @Bean
//    public DockerClientConfig dockerClientConfig() {
//        return DefaultDockerClientConfig.createDefaultConfigBuilder()
//                .withDockerHost("IP_where_docker_daemon_is_running_with_port")
//                .withDockerTlsVerify("Transport_Layer_Security_accepts_Boolean")
//                .withDockerCertPath("If_tls_is_true")
//                .build();
//    }

    @Bean
    public DockerClient dockerClient() {
        String localDockerHost = SystemUtils.IS_OS_WINDOWS ? "tcp://localhost:2375" : "unix:///var/run/docker.sock";
        return DockerClientBuilder.getInstance(localDockerHost)
//                .withDockerCmdExecFactory(nettyDockerCmdExecFactory())
                .build();

    }
}
