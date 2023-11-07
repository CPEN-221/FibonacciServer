package fibonacci;

public class Configs {
    // reqCount is the number of requests to send to the server
    // requests are sent in order from 1 to reqCount inclusive
    public static final int reqCount = 100;

    // FIBONACCI_PORT is the port number on which the server listens
    public static final int FIBONACCI_PORT = 4949;

    // serverPerReqSleepMilliseconds is the time (in milliseconds) that the server sleeps after processing each request
    public static int serverPerReqSleepMilliseconds = 10;
}
