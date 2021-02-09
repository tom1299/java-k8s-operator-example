package com.jeeatwork;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

public class K8sClientTest {

    @Test
    @EnabledIfSystemProperty(named = "e2eTest", matches = "true")
    public void testListNamespaces() {
        String masterUrl = System.getProperty("k8s.master.url");
        Config config = new ConfigBuilder().withMasterUrl(masterUrl)
                .build();
        KubernetesClient client = new DefaultKubernetesClient(config);
        client.namespaces().list().getItems().forEach(namespace -> System.out.println(namespace.getMetadata().getName()));
    }
}
