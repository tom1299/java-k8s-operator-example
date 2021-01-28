package com.jeeatwork.k8s;

import com.slack.api.bolt.App;
import com.slack.api.bolt.jetty.SlackAppServer;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

public class SlackAppProvider {

    @Produces
    @Singleton
    App newApp() {
        return new App();
    }

}
