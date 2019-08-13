package manifestservice;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.FileNotFoundException;
import java.util.Date;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import properties.Config;

public class ServiceCron {
	 static Logger LOG = LoggerFactory.getLogger(ServiceCron.class);

    public void run() throws Exception {
        String cronExpression = Config.PROPERTIES.getString("quartz.cronexpression");

        // First we must get a reference to a scheduler
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();
        // job 1 will run every 1800 seconds
        JobDetail job = newJob(ServiceJob.class).withIdentity("Traitement des manifestes EXPORT", "MANIFESTE EXPORT").build();

        CronTrigger trigger = newTrigger().withIdentity("trigger1", "Groupe 1").withSchedule(cronSchedule(cronExpression))//"0/30 * * * * ?"))
                .build();

        Date ft = sched.scheduleJob(job, trigger);
		    LOG.info(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
		             + trigger.getCronExpression());
        sched.start();

		    LOG.info("------- Started Scheduler -----------------");
    }

    public static void main(String[] args) {

        ServiceCron cron = new ServiceCron();
        try {
            cron.run();
        } catch (Exception e) {
				LOG.info("ERROR");
            e.printStackTrace();
        }
    }

}
