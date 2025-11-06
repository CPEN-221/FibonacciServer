package fibonacci;

public class Configs {
    // reqCount is the number of requests to send to the server
    // requests are sent in order from 1 to reqCount inclusive
    public static final int reqCount = 100;

    // FIBONACCI_PORT specifies the port on which the Fibonacci server listens.
    // Valid port numbers range from 1 to 65535 [a 16-bit unsigned integer].
    // Port 0 is reserved and cannot be used in TCP.
    // Choose a port that is not already in use by another process.
    public static final int FIBONACCI_PORT = 4949;

    // serverPerReqSleepMilliseconds is the time (in milliseconds) that the server sleeps after processing each request
    public static int serverPerReqSleepMilliseconds = 50;
}
