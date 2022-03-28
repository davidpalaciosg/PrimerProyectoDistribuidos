package com.ejemplospubsub;

import java.util.Random;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

public class Publisher
{
    public static void main(String[] args) throws Exception
    {
		try (ZContext context = new ZContext()){
			//Socket PUB
			ZMQ.Socket publisher = context.createSocket(SocketType.PUB);
			//Socket con puerto
			publisher.bind("tcp://*:5556");
			publisher.bind("ipc://weather");
			
			//Números random
			Random srandom = new Random(System.currentTimeMillis());
			while(!Thread.currentThread().isInterrupted())
			{
				int zipcode, temperature, relhumidity;
				zipcode = 10000 + srandom.nextInt(10000);
				temperature = srandom.nextInt(215) -80 + 1;
				relhumidity = srandom.nextInt(50) - 10 + 1;
				
				String update = String.format(
					"%05d %d %d", zipcode, temperature, relhumidity
				);
				
				publisher.send(update,0);
			}
		}
	}
}