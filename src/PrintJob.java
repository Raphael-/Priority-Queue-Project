public class PrintJob implements Comparable<PrintJob>
{
	private int id, size, waitingTime, priority, arrivalTime;
	private static int nextId = 0; //ID counter, used to number the printJobs
	
	/*
	* Constructor
	*/
	public PrintJob(int size, int waitingTime, int priority, int arrivalTime)
	{
		this.id = ++nextId;
		setSize(size);
		setWaitingTime(waitingTime);
		setPriority(priority);
		setArrivalTime(arrivalTime);
	}
	
	/*
	* get and set methods
	*/
	
	public int getId()
	{
		return this.id;
	}
	
	public int getSize()
	{
		return this.size;
	}
	
	public void setSize(int size)
	{
		if(size>0 && size<129)
		{
			this.size=size;
		}
		else
		{
			System.err.print("PrintJob's size out of bounds.");
			System.exit(-1); //exit call with param -1 means abnormal termination
		}
	}
	
	public int getWaitingTime()
	{
		return this.waitingTime;
	}
	
	public void setWaitingTime(int waitingTime)
	{
		if(waitingTime<0)
		{	
			System.err.println("Cannot set negative waiting time value.");
			System.exit(-1);
		}
		else
		{
			this.waitingTime=waitingTime;
		}
			
	}
	
	public int getArrivalTime()
	{
		return this.arrivalTime;
	}
	
	public void setArrivalTime(int arrivalTime)
	{
		if(arrivalTime<0)
		{	
			System.err.println("Cannot set negative arrival time value.");
			System.exit(-1);
		}
		else
		{
			this.arrivalTime=arrivalTime;
		}
			
	}
	
	public int getPriority()
	{
		return this.priority;
	}
	
	public void setPriority(int priority)
	{
		if(priority>=0 && priority<=127)
		{
			this.priority=priority;
		}
		else
		{
			System.err.print("PrintJob's priority out of bounds.");
			System.exit(-1);
		}
	}

	public String toString()
	{
		return "Id : "+this.id+" Size : "+this.size+" Waiting Time : "+this.waitingTime+" Priority : "+this.priority+" Arrival Time "+this.arrivalTime;
	}
	
	public static void resetId()
	{
		nextId=0;
	}
	
	/*
	* Compares priorities through the Comparable implementation
	*/
	public int compareTo(PrintJob p) 
	{
		if(this.getPriority() < p.getPriority())
		{
			return -1;
		}
		else if(this.getPriority() == p.getPriority())
		{
			return this.compareSamePriority(p);
		}
		else
		{
			return 1;
		}
	}
	
	/*
	* printJobs with the same priority are compared by their arrival time
	*/
	private int compareSamePriority(PrintJob p)
	{
		if(this.getArrivalTime()<p.getArrivalTime())
			return 1;
		
		else if(this.getArrivalTime()==p.getArrivalTime())
			return 0;
		
		else
			return -1;
	}
}
