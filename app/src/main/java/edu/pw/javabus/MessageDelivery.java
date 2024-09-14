package edu.pw.javabus;

/**
 * Handles the way messages are delivered. See {@link InMemoryBus} how it handles the delivery.
 */
interface MessageDelivery {
    /**
     * Informs the loop if this consumer should be notified with message.
     * @param consumer the consuming object
     * @param message the message that is about to send
     * @param anySuccess true if any other consumer so far received the message with success
     * @return true if the loop should pass the message to this consumer
     */
    boolean shouldCallThisConsumer(Consumer<? extends Message> consumer, Message message, boolean anySuccess);

    /**
     * Informs the loop if it should stop before end of iteration. It's called only after sending the message.
     * @param isLastSuccess true if the last message has been sent with success
     * @return true if the loop should stop and no other message shall be sent, false if continuing the loop
     */
    boolean shouldStop(boolean isLastSuccess);

    /**
     * Informs the loop whether if should get parent topic of the current one.
     * @param isAnySuccess true if any message so far has been delivered with success
     * @return true if the loop should get parent topic and start another loop of the parent topic
     */
    boolean shouldGetParentTopic(boolean isAnySuccess);
}
