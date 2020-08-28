package msc.dispatcher.state;

public class StateReporterExecutor implements Runnable {

    StateClient client;

    public StateReporterExecutor(StateClient client) {
        this.client = client;
    }

    @Override
    public void run() {
        client.post();
    }
}
