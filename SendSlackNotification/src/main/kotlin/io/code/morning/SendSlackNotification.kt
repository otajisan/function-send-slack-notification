package io.code.morning

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result

/**
 * Handler for requests to Lambda function.
 */
class SendSlackNotification : RequestHandler<Any, Any> {

    override fun handleRequest(input: Any, context: Context): Any {
        return sendSlackMessage()
    }

    fun sendSlackMessage() {
        val mapper = jacksonObjectMapper()
        val webhookUrl: String = "https://hooks.slack.com/services/xxx/xxx/xxx"
        val body: String = mapper.writeValueAsString(SlackMessage(
                text = "Hello Lambda"
        ))

        val (_, _, response) = Fuel.post(webhookUrl).body(body).responseString()

        when (response) {
            is Result.Failure -> {
                response.getException().toString()
            }
            is Result.Success -> {
                response.get()
            }
        }
    }

    data class SlackMessage(
            @JsonProperty("text") val text: String
    )
}
