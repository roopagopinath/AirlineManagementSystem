package project.AirlineSystem.Employee;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;


public class ConnectionPool {

	private static final int MAX_POOL_SIZE=10;
	private static int CURRENT_POOL_SIZE;
	public static Vector<Connection> connectionList=new Vector<Connection>(MAX_POOL_SIZE);
	
	public synchronized static Connection getConnection(String url,String username,String password) throws 

SQLException
	{
	    	if(connectionList.isEmpty())
	    	{
	    		if(CURRENT_POOL_SIZE>=MAX_POOL_SIZE)
	    		{
	    			throw new MaxPoolReachedException("Maximum pool size reached cannot create connections");
	    		}
	    		CURRENT_POOL_SIZE++;
	    		System.out.println("creating new connection");
				return DriverManager.getConnection(url, username,password);
				
	    	}
	    	else
	    	{
	    		CURRENT_POOL_SIZE--;
	    		System.out.println("returning from existing connection pool");
	    		return connectionList.get(0);
	    	}
	}
	
	public synchronized static void addConnectionToPool(Connection con){
		System.out.println("adding back to connection pool");
		connectionList.add(con);
		
	}
	
	
}
