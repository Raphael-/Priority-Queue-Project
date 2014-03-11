import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Random;


public class AlgorithmA 
{
    public static int time=0;//global timer
	public static int maxWT=0;// max waiting time
	public static double totalWT=0.0;//total waiting time (to calculate average waiting time)
	public static int completedPJ=0;//how many print jobs have been completed 
	protected static String inputFile; //The file that contains the incoming printjobs
	protected static String outputFile; //The file that the report of the algorithm will be written
	private static String maxMsg; //Maximum waiting time message,that will be printed in the report
	public static LinkedList<PrintJob> pjs ;  //linked list containing the print jobs
	public static MaxPQ<PrintJob> pq ;
	public static void runA() throws IOException
	{
		pjs = new LinkedList<PrintJob>("PrintJobs");;
		readFile();
		
		//make the priority queue with the linked list's size and the required comparator
		pq = new MaxPQ<PrintJob>(pjs.getSize(),new PrintJobComparator());
		//update the time after the first arrival
		clockTicks(pjs.getFirst().getArrivalTime());
		//while there are still print jobs remaining, add them to the queue and "print" them
		for(;!pjs.isEmpty();)
		{
			sendToBuffer();
			doPrint();
		}
		reset();
	}
	
	/*
	 * Resets all variables for future use
	 */
	private static void reset()
	{
		PrintJob.resetId();
		maxWT=0;
		completedPJ=0;
		time=0;
		totalWT=0.0;
		maxMsg="";
		pq=null;
		pjs=null;
	}
	
	/*
	*  Fills the print job linked list
	*/
	private static void readFile()
	{
		//line read
		String line=null;
		//String array containing (for each line read) the arrival time and size
		String [] tokens=null;
		//variable for checking the arrival times
		int previous=0;
		try 
		{
			//open the inputFile
			BufferedReader br=new BufferedReader(new FileReader(inputFile));
			//read while the next line is not null
			while((line=br.readLine())!=null)
			{
				tokens=line.split(" ");  //"splits" the line to tokens using the " " string as the delimiter
				
				if(previous<=Integer.parseInt(tokens[0]))
				{
					//if the arrival time sequence is still right, make a new PrintJob
					//parameters: tokens[1] contains its size, its waiting time is now 0, its priority equals to 128 minus its size, tokens[0] contains its arrival time
					pjs.insertAtBack(new PrintJob(Integer.parseInt(tokens[1]),0,(128-Integer.parseInt(tokens[1])),Integer.parseInt(tokens[0])));
					previous=Integer.parseInt(tokens[0]);
				}
				else
				{
					//bad arrival times' sequence
					System.err.println("Error@line "+line+"\nTime value is lower than the previous object.");
					//end execution with system.exit (parameter -1 indicates abnormal termination)
					System.exit(-1);
				}
			}
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	private static void clockTicks(int n) // n: seconds to add to the time variable
	{
		time+=n;
	}
	
	/*
	*  Sends the print jobs from the list to the queue
	*/
	private static void sendToBuffer()
	{
		if(time >= pjs.getFirst().getArrivalTime())
		{
			//check if the time is ahead of the last arrival time
			while(time >= pjs.getFirst().getArrivalTime())
			{
				//while the time is up-to-date retrieve the next print job, update its waiting time and send it to the queue
				PrintJob pj = pjs.removeFromFront();
				pj.setWaitingTime(time-pj.getArrivalTime());
				pq.insert(pj);
				if(pjs.isEmpty()) //no print jobs left
					break;
			}
		}
		else
		{
			//if no print jobs have arrived, update the time to the last arrival time 
			clockTicks(pjs.getFirst().getArrivalTime()-time);
		}
	}
	
	/*
	*  Total and maximum waiting time calculated
	*/
	private static void doPrint()
	{
		PrintJob pj = null;
		while(!pq.empty())
		{
			//get the next print job, update its waiting time
			pj = pq.remove();
			pj.setWaitingTime(time-pj.getArrivalTime());
			totalWT += pj.getWaitingTime();
			if(maxWT < pj.getWaitingTime())
			{
				maxWT = pj.getWaitingTime();
				maxMsg = "Maximum waiting time = " + maxWT + ", achieved by file " + pj.getId();
			}
			completedJob(pj);
			if(!pjs.isEmpty())
			{
				//if while printing a new print job has arrived, break the loop to add it. This check is necessary to ensure
				//that while printing the printJobs, there is no smaller in size printJob that has arrived
				if(time>=pjs.getFirst().getArrivalTime()) 
					break;
			}
		}
	}
	
	/*
	*  Tick the clock after the print job is processed, add to the outputFile, add to the completed jobs' counter
	*/
	private static void completedJob(PrintJob p)
	{
		clockTicks(p.getSize());  //PrintJob's size represents the time needed to complete the printjob
		writeReport(p);
		completedPJ++;
	}
	
	/*
	*  Makes and writes to the desired outputFile
	*/
	private static void writeReport(PrintJob p)
	{
		BufferedWriter bw=null;
		DecimalFormat df = new DecimalFormat("0.00"); //round off to 2 decimal places
		df.setMinimumFractionDigits(2);
		try
		{
			//make the outputFile
			bw=new BufferedWriter(new FileWriter(outputFile,true));
			bw.write("t= " + time + ", completed file " + p.getId());
			bw.newLine();
			if(pjs.getSize()==0 && pq.empty()) 
			{
				//If both the list and the queue are empty, it's time to display Average and Max waiting time values
				//Which happens when the last print job is read
				bw.write("Average Waiting Time : " + df.format(totalWT/completedPJ));
				bw.newLine();
				bw.write(maxMsg);
			}
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
				try 
				{
					bw.close();
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
		}
		
	}
}
