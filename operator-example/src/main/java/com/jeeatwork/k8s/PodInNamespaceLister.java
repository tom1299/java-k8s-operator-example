package com.jeeatwork.k8s;

import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class PodInNamespaceLister {

    @Inject
    KubernetesClient k8sClient;

    @Inject
    MethodsClient slackClient;

    String channel;

    String namespace;

    String lastPodsHash;

    @Scheduled(every="30s")
    void listPods() throws Exception {
        List<Pod> podList =  k8sClient.pods().inNamespace(namespace).list().getItems();
        System.out.println("Found " + podList.size() + " pod");

        if (podList.size() == 0) {
            System.out.printf("No pods found in namespace %s", namespace);
        }

        List<String> currentPods = podList.stream().map(pod -> pod.getMetadata().getName())
                .collect(Collectors.toList());
        Collections.sort(currentPods);

        String newPodsHash = calculateHash(currentPods);

        if (hasPodListChanged(newPodsHash)) {
            sendMessage(currentPods);
            lastPodsHash = newPodsHash;
        }
        else {
            System.out.println("Pod list has not changed");
        }
    }

    void sendMessage(List<String> currentPods) throws Exception {
        System.out.printf("Pod list has changed, sending message to channel %s", channel);
        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .channel(channel)
                .text(createPodListMessage(currentPods))
                .mrkdwn(true)
                .build();
        ChatPostMessageResponse response = slackClient.chatPostMessage(request);
        System.out.println(response);
    }

    String calculateHash(List<String> currentPods) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        currentPods.forEach(pod -> messageDigest.update(pod.getBytes()));
        return Base64.getEncoder().encodeToString(messageDigest.digest());
    }

    boolean hasPodListChanged(String newPodsHash) {
        System.out.printf("Last Pods hash is %s, new pods hash is %s%n", lastPodsHash, newPodsHash);
        return lastPodsHash == null || !lastPodsHash.equals(newPodsHash);
    }

    String createPodListMessage(List<String> currentPods) {
        StringBuilder message = new StringBuilder("*Pods* currently deployed in `");
        message.append(namespace);
        message.append("`:\n");
        currentPods.forEach(pod -> message.append("• " + pod + "\n"));
        return message.toString();
    }

    void onStartup(@Observes StartupEvent _ev) {
        System.out.println("Starting...");
        channel = System.getenv("SLACK_CHANNEL");
        namespace = System.getenv("K8S_NAMESPACE");
    }
}
