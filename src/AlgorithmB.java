import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Random;


public class AlgorithmB 
{
    private static int time=0; //global timer
	private static int maxWT=0; // max waiting time
	private static double totalWT=0.0; //total waiting time (to calculate average waiting time)
	private static int completedPJ=0; //how many print jobs have been completed 
	protected static String inputFile; //The file that contains the incoming printjobs
	protected static String outputFile; //The file that the report of the algorithm will be written
	private static String maxMsg=null; //Maximum waiting time message,that will be printed in the report
	private static LinkedList<PrintJob> pjs; //linked list containing the print jobs 
	private static MaxPQ<PrintJob> pq ; //Priority Queue that will work as a buffer
	
	public static void runB()
	{
		pjs=new LinkedList<PrintJob>("PrintJobs");
		readFile(); //Reading the input file
		
		//make the priority queue with the linked list's size and the required comparator
		pq=new MaxPQ<PrintJob>(pjs.getSize(),new PrintJobComparator()); 
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
		totalWT=0.0;
		time=0;
		maxMsg="";
		pq=null;
		pjs=null;
	}
	
	/*
	 * Increase timer by n
	 * Adds to the timer second by second so as to check for update times
	 */
	private static void clockTicks(int n)
	{
		while(n>0)
		{
			time++;
			n--;
			//if it is time to update the priorities (time=15,30,45,..,etc.)
			if(time%15==0) update(); //call the update method
		}
	}
	
	/*
	 * Returns the variable with the minimum value.
	 */
	private static int min(int a,int b)
	{
		return a<b?a:b;
	}
	
	/* 
	 * Fills the print job linked list
	 * Identical with AlgorithmA readFile method
	 */
	private static void readFile()
	{
		String line=null;
		String [] tokens=null;
		int previous=0;
		try 
		{
			BufferedReader br=new BufferedReader(new FileReader(inputFile));
			while((line=br.readLine())!=null)
			{
				tokens=line.split(" ");

				if(previous<=Integer.parseInt(tokens[0]))
				{
					pjs.insertAtBack(new PrintJob(Integer.parseInt(tokens[1]),0,(128-Integer.parseInt(tokens[1])),Integer.parseInt(tokens[0])));
					previous=Integer.parseInt(tokens[0]);
				}
				else
				{
					System.err.println("Error@line "+line+"\nTime value is lower than the previous object.");
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
	
	/*
	*  Sends the print jobs from the list to the queue
	*  Identical with AlgorithmA sendToBuffer method
	*/
	private static void sendToBuffer()
	{
			if(time>=pjs.getFirst().getArrivalTime())
			{
				while(time>=pjs.getFirst().getArrivalTime())
				{
					PrintJob pj=pjs.removeFromFront();
					pj.setWaitingTime(time-pj.getArrivalTime());
					pq.insert(pj);
					if(pjs.isEmpty())
						break;
				}
			}
			else
			{
				clockTicks(pjs.getFirst().getArrivalTime()-time);
			}
	}
	
	/*
	*  Total and maximum waiting time calculated
	*  Identical with AlgorithmA doPrint method
	*/
	private static void doPrint()
	{
		PrintJob pj=null;
		while(!pq.empty())
		{
			//pq.print();
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
				if(time >= pjs.getFirst().getArrivalTime()) 
					break;	
			}
		}
	}
	
	/*
	*  Tick the clock after the print job is processed, add to the outputFile, add to the completed jobs' counter
	*  Identical with AlgorithmA completedJob method
	*/
	private static void completedJob(PrintJob p)
	{
		clockTicks(p.getSize()); //PrintJob's size represents the time needed to complete the printjob.
		writeReport(p);
		completedPJ++;
	}
	
	/*
	*  Makes and writes to the desired outputFile
	*  Identical with AlgorithmA writeReport method
	*/
	private static void writeReport(PrintJob p)
	{
		BufferedWriter bw=null;
		DecimalFormat df = new DecimalFormat("0.00");
		df.setMinimumFractionDigits(2);
		try
		{
			bw=new BufferedWriter(new FileWriter(outputFile,true));
			bw.write("t= " + time + ", completed file " + p.getId());
			bw.newLine();
			if(pjs.getSize()==0 && pq.empty()) 
			{
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
	
	/*
	 * Updates the priorities of PrintJob objects
	 * When 15 seconds have elapsed, this method will be called to
	 * update the priorities. A temp linked list is used to transfer
	 * PrintJobs so as to sort the PQ with the new priorities.
	 */
	private static void update()
	{
		PrintJob temp = null; //Temp object that holds a PrintJob while it's being removed from the PQ and edited
		LinkedList<PrintJob> list = new LinkedList<PrintJob>(); //Temp LinkedList to store the PrintJobs
		while(!pq.empty()) //while PQ contains more PrintJobs
		{
			temp = pq.remove();
			temp.setWaitingTime(time-temp.getArrivalTime()); //update the waiting time
			temp.setPriority(min(127, temp.getPriority() + temp.getWaitingTime())); //set the new priority according to the given formula
			list.insertAtFront(temp); //insert the object into the temp list
		}
		while(!list.isEmpty()) 
		{
			pq.insert(list.removeFromFront()); //Transfer every PrintJob from the list back to the PQ
		}
	}
}
