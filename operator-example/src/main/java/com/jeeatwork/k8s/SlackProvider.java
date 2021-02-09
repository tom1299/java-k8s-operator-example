package com.jeeatwork.k8s;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

public class SlackProvider {

    @Produces
    @Singleton
    MethodsClient newSlackClient() {
        Slack slack = Slack.getInstance();
        String slackBotToken = System.getenv("SLACK_BOT_TOKEN");
        MethodsClient methods = slack.methods(slackBotToken);
        return methods;
    }

}
