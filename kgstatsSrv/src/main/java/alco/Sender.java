package alco;

public class Sender {

    public void send(ParentDTO parent) {
        System.out.println("Parent send method");
    }

    public void send(CounterDTO counter) {
        System.out.println("Counter send method");
    }

    public void send(FlowMeterDTO flowMeter) {
        System.out.println("Flow meter send method");
    }
}
