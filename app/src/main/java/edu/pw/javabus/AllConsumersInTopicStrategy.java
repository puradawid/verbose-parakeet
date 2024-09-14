package edu.pw.javabus;

public class AllConsumersInTopicStrategy implements MessageDelivery {

    @Override
    public boolean shouldStop(boolean isLastSuccess) {
        return false;
    }

    @Override
    public boolean shouldGetParentTopic(boolean isAnySuccess) {
        return true;
    }

    @Override
    public boolean shouldCallThisConsumer(Consumer<? extends Message> consumer, Message message, boolean anySuccess) {
        return true;
    }
}
