package com.jeeatwork.k8s;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PodInNamespaceListerTest {

    @Test
    public void testCalculateHash1() throws Exception {
        PodInNamespaceLister lister = new PodInNamespaceLister();
        String hash = lister.calculateHash(List.of("a", "b", "c"));
        assertNotNull(hash);
    }

    @Test
    public void testHasPodListChanged() throws Exception {
        PodInNamespaceLister lister = new PodInNamespaceLister();
        String lastHash = lister.calculateHash(List.of("a", "b", "c"));
        lister.lastPodsHash = lastHash;
        String newHash = lister.calculateHash(List.of("a"));
        assertTrue(lister.hasPodListChanged(newHash));
    }

    @Test
    public void testHasPodListChanged2() throws Exception {
        PodInNamespaceLister lister = new PodInNamespaceLister();
        String lastHash = lister.calculateHash(List.of("a", "b", "c"));
        lister.lastPodsHash = lastHash;
        String newHash = lister.calculateHash(List.of("a", "b", "c"));
        assertFalse(lister.hasPodListChanged(newHash));
    }

    @Test
    public void testCreatePodListMessage() {
        PodInNamespaceLister lister = new PodInNamespaceLister();
        String message = lister.createPodListMessage(List.of("Pod1", "Pod2", "Pod3"));
        assertNotNull(message);
    }

}