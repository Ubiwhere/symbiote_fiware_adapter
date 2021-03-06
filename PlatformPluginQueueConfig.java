/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.h2020.symbiote.fiwarePlugin;

import eu.h2020.symbiote.resources.RapDefinitions;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 *
 * @author Matteo Pardi <m.pardi@nextworks.it>
 */
@Conditional(PlatformSpecificPluginCondition.class)
@Configuration
public class PlatformPluginQueueConfig {   
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Autowired
    @Qualifier(RapDefinitions.PLUGIN_REGISTRATION_EXCHANGE_IN)    
    TopicExchange exchange;
    
    
    @Bean(name=PlatformSpecificPluginFiware.PLUGIN_RES_ACCESS_QUEUE)
    Queue specificPluginQueue() {
        return new Queue(PlatformSpecificPluginFiware.PLUGIN_RES_ACCESS_QUEUE, false);
    }

    @Bean(name=PlatformSpecificPluginFiware.PLUGIN_RES_ACCESS_QUEUE + "Bindings")
    Binding specificPluginBindings(@Qualifier(PlatformSpecificPluginFiware.PLUGIN_RES_ACCESS_QUEUE) Queue queue,
                                   @Qualifier(RapDefinitions.PLUGIN_EXCHANGE_OUT) TopicExchange exchange) {
        
        return BindingBuilder.bind(queue).to(exchange).with(PlatformSpecificPluginFiware.PLUGIN_PLATFORM_ID + ".*");
    }

    @Bean(name=PlatformSpecificPluginFiware.PLUGIN_RES_ACCESS_QUEUE + "Container")
    SimpleMessageListenerContainer specificPluginContainer(ConnectionFactory connectionFactory,
                                             @Qualifier(PlatformSpecificPluginFiware.PLUGIN_RES_ACCESS_QUEUE + "Listener") MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(PlatformSpecificPluginFiware.PLUGIN_RES_ACCESS_QUEUE);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @DependsOn(RapDefinitions.PLUGIN_REGISTRATION_EXCHANGE_IN)
    @Bean
    PlatformSpecificPluginFiware specificPluginReceiver() {
        // it needs to know the exchange where to send the registration message
        return new PlatformSpecificPluginFiware(rabbitTemplate, exchange);
    }

    @Bean(name=PlatformSpecificPluginFiware.PLUGIN_RES_ACCESS_QUEUE + "Listener")
    MessageListenerAdapter specificPluginListenerAdapter(PlatformSpecificPluginFiware receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }
}
