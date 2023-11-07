package fibonacci;

import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * FibonacciServerMulti is a server that finds the n^th Fibonacci number given
 * n. It accepts requests of the form: Request ::= Number "\n" Number ::= [0-9]+
 * and for each request, returns a reply of the form: Reply ::= (Number | "err")
 * "\n" where a Number is the request Fibonacci number, or "err" is used to
 * indicate a misformatted request. FinbonacciServerMulti can handle multiple
 * concurrent clients.
 */
public class FibonacciServerMulti {
	/** Default port number where the server listens for connections. */
	public static final int FIBONACCI_PORT = Configs.FIBONACCI_PORT;
	static int serverPerReqSleepMilliseconds = Configs.serverPerReqSleepMilliseconds;

	private ServerSocket serverSocket;

	// Rep invariant: serverSocket != null
	//
	// Thread safety argument:
	// TODO FIBONACCI_PORT
	// TODO serverSocket
	// TODO socket objects
	// TODO readers and writers in handle()
	// TODO data in handle()

	/**
	 * Make a FibonacciServerMulti that listens for connections on port.
	 * 
	 * @param port
	 *            port number, requires 0 <= port <= 65535
	 */
	public FibonacciServerMulti(int port) throws IOException {
		serverSocket = new ServerSocket(port);
	}

	/**
	 * Run the server, listening for connections and handling them.
	 * 
	 * @throws IOException
	 *             if the main server socket is broken
	 */
	public void serve() throws IOException {
		while (true) {
			// block until a client connects
			final Socket socket = serverSocket.accept();
			// create a new thread to handle that client
			Thread handler = new Thread(new Runnable() {
				public void run() {
					try {
						try {
							handle(socket);
						} finally {
							socket.close();
						}
					} catch (IOException ioe) {
						// this exception wouldn't terminate serve(),
						// since we're now on a different thread, but
						// we still need to handle it
						ioe.printStackTrace();
					}
				}
			});
			// start the thread
			handler.start();
		}
	}

	/**
	 * Handle one client connection. Returns when client disconnects.
	 * 
	 * @param socket
	 *            socket where client is connected
	 * @throws IOException
	 *             if connection encounters an error
	 */
	private void handle(Socket socket) throws IOException {
		System.err.println("client connected");

		// get the socket's input stream, and wrap converters around it
		// that convert it from a byte stream to a character stream,
		// and that buffer it so that we can read a line at a time
		BufferedReader in = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));

		// similarly, wrap character=>bytestream converter around the
		// socket output stream, and wrap a PrintWriter around that so
		// that we have more convenient ways to write Java primitive
		// types to it.
		PrintWriter out = new PrintWriter(new OutputStreamWriter(
				socket.getOutputStream()), true);

		try {
			// each request is a single line containing a number
			for (String line = in.readLine(); line != null; line = in
					.readLine()) {
				System.err.println("request: " + line);
				try {
					int x = Integer.valueOf(line);
					// compute answer and send back to client
					BigInteger y = fibonacci(x);
					System.err.println("reply: " + y);
					out.println(y);
				} catch (NumberFormatException e) {
					// complain about ill-formatted request
					System.err.println("reply: err");
					out.print("err\n");
				}
				// important! our PrintWriter is auto-flushing, but if it were
				// not:
				// out.flush();
				if (serverPerReqSleepMilliseconds>0) {
					try {
						Thread.sleep(serverPerReqSleepMilliseconds);
					} catch (Exception e) {
						System.err.println("sleep not working");
					}
				}
			}
		} finally {
			out.close();
			in.close();
		}
	}

	/**
	 * Compute the n^th Fibonacci number
	 * 
	 * @param n
	 *            indicates the Fibonacci number to compute. Requires n > 0.
	 * @return the n^th Fibonacci number
	 */
	public BigInteger fibonacci(int n) {
		if (n <= 0)
			return BigInteger.valueOf(0);
		if (n == 1)
			return BigInteger.valueOf(1);

		// if n >= 2, do the following

		BigInteger a = BigInteger.valueOf(0);
		BigInteger b = BigInteger.valueOf(1);
		BigInteger c;

		while (n > 1) {
			c = b;
			b = a.add(b);
			a = c;
			n--;
		}
		return b;
	}

	/**
	 * Start a FibonacciServerMulti running on the default port.
	 */
	public static void main(String[] args) {
		try {
			FibonacciServerMulti server = new FibonacciServerMulti(
					FIBONACCI_PORT);
			server.serve();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
