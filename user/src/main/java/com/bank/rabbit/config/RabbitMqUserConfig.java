package com.bank.rabbit.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqUserConfig {

    public static final String EXCHANGE_NAME = "bank-events-exchange";
    public static final String OTP_VERIFIED_QUEUE = "otp-verified-queue";

    public static final String ROUTING_USER_REGISTERED = "user.registered";
    public static final String ROUTING_OTP_VERIFIED = "otp.verified";

    @Bean("userBankExchange")
    public TopicExchange bankExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue otpVerifiedQueue() {
        return new Queue(OTP_VERIFIED_QUEUE);
    }

    @Bean
    public Binding bindingOtpVerified(Queue otpVerifiedQueue, @Qualifier("otpBankExchange") TopicExchange bankExchange) {
        return BindingBuilder.bind(otpVerifiedQueue).to(bankExchange).with(ROUTING_OTP_VERIFIED);
    }
}
