package org.usfirst.frc.team449.robot._2020.general.pubsub;

public interface Publisher<Message> {
  Channel<Message> getChannel();

  default void publish(Message message) {
    getChannel().inject(message);
  }
}
