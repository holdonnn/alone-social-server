package social.alone.server.slack

import lombok.extern.slf4j.Slf4j
import org.springframework.core.env.Environment
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import social.alone.server.config.AppProperties

@Component
@Slf4j
class SlackNotifier(
        val slackClient: RestTemplate,
        val env: Environment,
        var appProperties: AppProperties
) {

    @Async
    fun send(text: String) {
        if (!listOf(*env.activeProfiles).contains("prod")) {
            return
        }
        val channel = appProperties.slack.channel ?: return
        val msg = Message(text)
        slackClient.postForEntity(channel, msg, String::class.java)
    }

    data class Message(val text: String)
}
