import java.io.File;
import java.io.IOException;

public class Comparisons 
{
	//static int counter=10; //Variable useful for write method
   	public static void main(String args[]) throws IOException 
	{
   			//./files is the directory of the random testing data .txt files
    		File directory = new File("./files");
    		String files[]=directory.list();
    		for(String s:files)
    		{
    			//while more input files exist, run algorithms A and B and save the resuls to the corresponding output files 
    			AlgorithmA.inputFile="./files/"+s;
    			AlgorithmA.outputFile=s.substring(0,s.indexOf(".txt"))+"_resultA.txt";
    			AlgorithmA.runA();
    			AlgorithmB.inputFile="./files/"+s;
    			AlgorithmB.outputFile=s.substring(0,s.indexOf(".txt"))+"_resultB.txt";
    			AlgorithmB.runB();
    		}
	}/*This method was used to create the random input files
   	private static void write(int lim) //creates random file contents.lim will be the limit of how many printjobs will be listed.
    {
    	//num is used to hold the arrival time 
        int d=1,num=0,cap=45; //cap is used to return a random number from [0,cap] and add it to num
        java.io.BufferedWriter bw=null; 
        java.util.Random rg = new java.util.Random(); //random numbers generator
        try
        {
           bw = new java.io.BufferedWriter(new java.io.FileWriter("./files/N"+lim+"_"+counter+".txt"));
           while(d<=lim)
           {
                   num=rg.nextInt(cap+1)+num; //random number between [0,cap+1)=[0,cap] will be generated.Then num will be added to the random number so it will be
                   //num=[previousNum,previousNum+randomNum]
                   bw.write(num+" "+(rg.nextInt(127)+1)); //random size of file between [1,127]
                   bw.newLine();
                   d++;
                   counter--;

           }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
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
    }*/
    
}
