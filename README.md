# Concurrent Unix Shell

This is a Java implementation of a concurrent Unix shell. 

## Features

- Implements common Unix shell commands like `ls`, `pwd`, `cat`, etc.
- Supports piping the output of one command to the input of another  
- Allows commands to run concurrently using Java threads
- Provides ability to run commands in the background using `&` 
- Includes `repl_jobs` command to list running background jobs
- Allows killing background jobs using `kill` command

## Implementation

- The shell is built around a REPL (read-eval-print-loop) model
- Commands are parsed using a `CommandBuilder`  
- Commands are executed by `Filter` classes that subclass `ConcurrentFilter`
- Piping is handled via `ConcurrentPipe` objects
- Threading and concurrency primitives like `Runnable`, `Thread`, and `LinkedBlockingQueue` are used

## Running the Shell

The shell can be launched by running the `ConcurrentREPL` class which contains the main method.

## Documentation

The code is documented with Javadoc comments.

## Author

tianl-h
