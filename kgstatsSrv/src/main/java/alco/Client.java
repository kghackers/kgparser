package alco;

public class Client {

    public static void main(String[] args) {
        Client client = new Client();

        client.send(new CounterDTO());
        client.send(new FlowMeterDTO());
    }

    public void send(ParentDTO dto) {
        Sender sender = new Sender();

        sender.send(dto);
    }
}
