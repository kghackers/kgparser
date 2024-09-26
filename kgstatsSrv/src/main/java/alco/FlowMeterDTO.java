package alco;

public class FlowMeterDTO implements ParentDTO {

    @Override
    public void send(Sender sender) {
        sender.send(this);
    }
}
