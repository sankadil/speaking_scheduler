

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class PlayListJob implements Job
{
	public void execute(JobExecutionContext context)
	throws JobExecutionException 

 {
		System.out.println("START : IT'S PLAYING....THIS IS MALKEY SPEAKING SCHEDULER !!! ");	
		new WavPlayer().start();
		System.out.println("END : IT'S PLAYING....THIS IS MALKEY SPEAKING SCHEDULER !!! ");	
	}
	
}
