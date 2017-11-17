Concurrency and the Fibonacci Server
===


This example illustrates the use of concurrent threads to process multiple requests from a server. The server is a process that receives a request to compute the n<sup>th</sup> Fibonacci number, which it then computes and returns to the requestor.

You can run the Fibonacci Server from the command line as `java fibonacci.FibonacciServer` and you should do this from the appropriate directory where the binary files are.

To run the Fibonacci Client, you would similarly use `java fibonacci.FibonacciClient`.

You can run the programs using your IDE but it is easier to visualize the multithreaded behaviour from the command line.
