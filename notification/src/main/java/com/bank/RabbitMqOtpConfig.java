package com.bank;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqOtpConfig {

    public static final String EXCHANGE_NAME = "bank-events-exchange";
    public static final String USER_REGISTERED_QUEUE = "user-registered-queue";

    public static final String ROUTING_USER_REGISTERED = "user.registered";
    public static final String ROUTING_OTP_VERIFIED = "otp.verified";

    @Bean
    public TopicExchange bankExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue userRegisteredQueue() {
        return new Queue(USER_REGISTERED_QUEUE);
    }

    @Bean
    public Binding bindingUserRegistered(Queue userRegisteredQueue, TopicExchange bankExchange) {
        return BindingBuilder.bind(userRegisteredQueue).to(bankExchange).with(ROUTING_USER_REGISTERED);
    }
}
