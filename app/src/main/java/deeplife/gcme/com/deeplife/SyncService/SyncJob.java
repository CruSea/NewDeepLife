package deeplife.gcme.com.deeplife.SyncService;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

/**
 * Created by bengeos on 12/24/16.
 */

public class SyncJob implements JobCreator {
    @Override
    public Job create(String tag) {
        switch (tag) {
            case SyncJobService.TAG:
                return new SyncJobService();
            default:
                return null;
        }
    }
}
