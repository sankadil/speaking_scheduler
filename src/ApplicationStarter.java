

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
//http://www.mkyong.com/java/quartz-2-scheduler-tutorial/
public class ApplicationStarter 
{
    public static void main( String[] args ) throws Exception
    {
    	JobDetail job = JobBuilder.newJob(PlayListJob.class).withIdentity("dummyJobName", "group1").build();
    	
    	Trigger trigger = TriggerBuilder
		.newTrigger()
		.withIdentity("dummyTriggerName", "group1")
		.withSchedule(
			CronScheduleBuilder.cronSchedule(PropertyUtil.getPropertyValue("cronSchedule"))) //0 0/30 * 1/1 * ? *  /*"0 0/30 * 1/1 * ?"*/
		.build();
    	
    	Scheduler scheduler = new StdSchedulerFactory().getScheduler();
    	scheduler.start();
    	scheduler.scheduleJob(job, trigger);
    
    }
}