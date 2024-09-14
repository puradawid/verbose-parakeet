package edu.pw.javabus;

import java.util.Iterator;

public class InMemoryBus implements Bus {

    private final RegisteredConsumers registeredConsumers = new RegisteredConsumers();

    private final MessageDelivery messageDelivery;

    InMemoryBus(MessageDelivery messageDelivery) {
        this.messageDelivery = messageDelivery;
    }

    @Override
    public <T extends Message> void register(Consumer<T> consumer, Topic topic, Class<T> messageType) {
        registeredConsumers.add(consumer, topic, messageType);
    }

    @Override
    public <T extends Message> void unregister(Consumer<T> consumer, Topic topic) {
        registeredConsumers.remove(consumer, topic);
    }

    @Override
    public <T extends Message> void send(Topic topic, T message) {
        boolean isLastDeliveryASuccess;
        boolean anySuccess = false;
        while (topic != null) {
            Iterator<Consumer<? extends Message>> iterator = registeredConsumers.find(topic, message.getClass()).iterator();
            while (iterator.hasNext()) {
                Consumer<? extends Message> nextConsumer = iterator.next();
                if (messageDelivery.shouldCallThisConsumer(nextConsumer, message, anySuccess)) {
                    isLastDeliveryASuccess = new ConsumeMethodObject(nextConsumer).consume(message).isSuccess();
                } else {
                    continue;
                }
                if (messageDelivery.shouldStop(isLastDeliveryASuccess)) {
                    return;
                }
                anySuccess = anySuccess || isLastDeliveryASuccess;
            }
            topic = messageDelivery.shouldGetParentTopic(anySuccess) ? topic.getParent() : null;
        }
    }

}
