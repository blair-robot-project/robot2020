package org.usfirst.frc.team449.robot._2020.general.pubsub;

public interface Subscriber<Message> {
  void notify(Message message);
}
