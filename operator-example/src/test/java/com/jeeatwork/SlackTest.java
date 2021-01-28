package com.jeeatwork;

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
        Slack slack = Slack.getInstance();
        String slackBotToken = System.getProperty("slack.bot.token");

        MethodsClient methods = slack.methods(slackBotToken);

        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .channel("C01KNA05BAA") // Use a channel ID `C1234567` is preferrable
                .text(":wave: Hi from a bot written in Java!")
                .build();

        ChatPostMessageResponse response = methods.chatPostMessage(request);
        System.out.println(response);
    }
}
