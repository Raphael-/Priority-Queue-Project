import java.util.Comparator;

final class PrintJobComparator implements Comparator<PrintJob>
{
	public int compare(PrintJob p1, PrintJob p2) 
	{
		return p1.compareTo(p2);
	}

}
