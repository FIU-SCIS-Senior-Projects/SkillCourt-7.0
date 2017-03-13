package fiu.com.skillcourt.connection;

/**
 * Created by pedrocarrillo on 10/14/16.
 */

public class Arduino {

    private String ip;
    private int port;
    private TYPE_LIGHT status;

    public enum TYPE_LIGHT {
        GREEN("0"), RED("1"), OFF("3"), FINISH("4"), DISCONNECTED("5"), START("6");

        private final String stringValue;

        private TYPE_LIGHT(final String s) { stringValue = s; }

        public String toString() { return stringValue; }
    }

    public Arduino(String ip, int port, TYPE_LIGHT status) {
        this.ip = ip;
        this.port = port;
        this.status = status;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public TYPE_LIGHT getStatus() {
        return status;
    }

    public void setStatus(TYPE_LIGHT status) {
        this.status = status;
    }

}
