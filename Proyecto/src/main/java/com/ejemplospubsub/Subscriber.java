package com.ejemplospubsub;

import java.util.Random;
import java.util.StringJoiner;
import java.util.StringTokenizer;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

public class Subscriber
{
    public static void main(String[] args) throws Exception
    {
		try (ZContext context = new ZContext()){
			
			System.out.println("Collection updates from weather server");
			//Socket SUB
			ZMQ.Socket subscriber = context.createSocket(SocketType.SUB);
			//Socket conectado al puerto
			subscriber.connect("tcp://localhost:5556");
			
			String filter = (args.length > 0) ? args[0]: "10001 ";
			
			subscriber.subscribe(filter.getBytes(ZMQ.CHARSET));
			
			int update_nbr;
			long total_temp = 0;
			for (update_nbr = 0; update_nbr < 100; update_nbr++)
			{
                String string = subscriber.recvStr(0).trim();
				System.out.println(string);
				StringTokenizer sscanf = new StringTokenizer(string, " ");
				int zipcode = Integer.valueOf(sscanf.nextToken());
				int temperature = Integer.valueOf(sscanf.nextToken());
				int relhumidity = Integer.valueOf(sscanf.nextToken());
				
				total_temp += temperature; 
			}
			
			System.out.println(
				String.format(
					"Avarage temperature for zipcode '%s' was %d. ", filter, (int)(total_temp / update_nbr)
				)
			);
			
				
			
		}
	}
}
