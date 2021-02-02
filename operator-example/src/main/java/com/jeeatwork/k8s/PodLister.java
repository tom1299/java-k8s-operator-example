package com.jeeatwork.k8s;

import com.slack.api.bolt.App;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.quarkus.scheduler.Scheduled;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class PodLister {

    @Inject
    private KubernetesClient client;

    @Inject
    private App app;

    @Scheduled(every="30s")
    void listPods() {
        List<Pod> podList = client.pods().list().getItems();
        System.out.println("Found " + podList.size() + " Pods:");
        for (Pod pod : podList) {
            System.out.println(" * " + pod.getMetadata().getName());
        }
    }

    void onStartup(@Observes StartupEvent _ev) {
        System.out.println("Starting...");
    }
}
