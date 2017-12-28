package atreides.house.arxiv_r;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;

/**
 * Created by the Kwisatz Haderach on 12/20/2017.
 */

public class UpdateService extends JobService {
    private static final String TAG = "SyncService";

    @Override
    public boolean onStartJob(JobParameters params) {
        //Intent service = new Intent(getApplicationContext(), LocalWordService.class);
        //getApplicationContext().startService(service);
        //Util.scheduleJob(getApplicationContext()); // reschedule the job
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

}