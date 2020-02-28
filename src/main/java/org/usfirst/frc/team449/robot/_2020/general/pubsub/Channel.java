package org.usfirst.frc.team449.robot._2020.general.pubsub;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Channel<Message> {

  private final List<Subscriber<Message>> subscribers = new ArrayList<>();

  public void inject(Message message) {
    subscribers.forEach(Subscriber::notify);
  }

  @SafeVarargs
  public final void addSubscribers(Subscriber<Message>... subs) {
    subscribers.addAll(Arrays.asList(subs));
  }

  public void removeSubscriber(Subscriber sub) {
    subscribers.remove(sub);
  }
}
