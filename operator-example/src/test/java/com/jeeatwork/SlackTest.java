package com.jeeatwork;

import static org.junit.jupiter.api.Assertions.assertTrue;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.io.IOException;

public class SlackTest {

    @Test
    @EnabledIfSystemProperty(named = "e2eTest", matches = "true")
    public void testSlack() throws IOException, SlackApiException {
        String slackBotToken = System.getProperty("slack.bot.token");
        String slackChannel = System.getProperty("slack.channel");

        Slack slack = Slack.getInstance();
        MethodsClient methods = slack.methods(slackBotToken);
        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .channel(slackChannel)
                .text("This is a list:\n• Entry1\n• Entry2\n• Entry3\n")
                .mrkdwn(true)
                .build();

        ChatPostMessageResponse response = methods.chatPostMessage(request);
        assertTrue(response.isOk());
    }
}
