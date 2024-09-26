package alco;

public class CounterDTO implements ParentDTO {

    @Override
    public void send(Sender sender) {
        sender.send(this);
    }
}
