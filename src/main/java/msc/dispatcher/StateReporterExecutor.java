package msc.dispatcher;

import msc.dispatcher.state.StateClient;

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
