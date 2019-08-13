package manifestservice;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceJob implements Job {
	 private static Logger LOG = LoggerFactory.getLogger(ServiceJob.class);
	 
	 public ServiceJob() {
	    }
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		ManifestService manServ = new ManifestService();
		JobKey jobKey = context.getJobDetail().getKey();
        LOG.info("<========Debut d'exécution du job: " + jobKey + " é  " + new Date()+"========>");
        try {
		manServ.runAction();
        } catch (Exception e) {
        	LOG.error(e.getMessage());
        }
		LOG.info("<========Fin d'exécution du job: " + jobKey + " é  " + new Date()+"========>");
	}
	

}
