package fibonacci;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Fibonaccis integers.
 */
class FibonacciFinder {

	private final BlockingQueue<Integer> in;
	private final BlockingQueue<FibonacciResult> out;

	// Rep invariant: in, out != null

	/**
	 * Make a FibonacciFinder that will listen for requests and generate replies.
	 * 
	 * @param requests
	 *            queue to receive requests from
	 * @param replies
	 *            queue to send replies to
	 */
	public FibonacciFinder(BlockingQueue<Integer> requests,
			BlockingQueue<FibonacciResult> replies) {
		this.in = requests;
		this.out = replies;
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
		}
		return b;
	}

	/**
	 * Start handling squaring requests.
	 */
	public void start() {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					// TODO: we may want a way to stop the thread
					try {
						// block until a request arrives
						int x = in.take();
						// compute the answer and send it back
						BigInteger y = fibonacci(x);
						out.put(new FibonacciResult(x, y));
					} catch (InterruptedException ie) {
						ie.printStackTrace();
					}
				}
			}
		}).start();
	}
}

/**
 * A squaring result message.
 */
class FibonacciResult {
	private final int input;
	private final BigInteger output;

	/**
	 * Make a new result message.
	 * 
	 * @param input
	 *            input number
	 * @param output
	 *            Fibonacci(input)
	 */
	public FibonacciResult(int input, BigInteger output) {
		this.input = input;
		this.output = output;
	}

	// TODO: we will want more observers, but for now...

	@Override
	public String toString() {
		return "fibonacci(" + input + ") = " + output;
	}
}

public class FibonacciQueue {

	/**
	 * Create and use a FibonacciFinder.
	 */
	public static void main(String[] args) {

		BlockingQueue<Integer> requests = new LinkedBlockingQueue<>();
		BlockingQueue<FibonacciResult> replies = new LinkedBlockingQueue<>();

		FibonacciFinder fibber = new FibonacciFinder(requests, replies);
		fibber.start();

		try {
			// make a request
			requests.put(42);

			// maybe do something concurrently...

			// read the reply
			System.out.println(replies.take());
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	}
}
