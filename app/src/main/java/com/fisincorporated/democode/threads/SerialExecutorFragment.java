package com.fisincorporated.democode.threads;

import android.view.View;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ericfoertsch on 2/1/16.
 */
public class SerialExecutorFragment extends ThreadDemoFragment implements View.OnClickListener {

    private static final String TAG = "SerialExecutorFragment";
    private static final String FRAGMENT_TITLE = "SerialExecutor";

    /**
     * For serial excution of background tasks
     */
    private static ExecutorService executor = Executors.newSingleThreadExecutor();
    ;
    private boolean okToExecute;


    // The following are called by the onClick() method in ThreadDemoFragment

    @Override
    protected String getFragmentTitle() {
        return FRAGMENT_TITLE;
    }

    /**
     * Start the serial processes
     */
    protected void doStart() {
//        mStartButton.setEnabled(false);
//        mCancelButton.setEnabled(true);
        executor.execute(new OkSerialExecutorRunnable());
        for (int i = 0; i < 4; ++i) {
            mTvStatusArea.append("Queuing process:" + i + sLineSeparator);
            executor.execute(new StartSerialExecuterRunnable(i));
        }
//        mStartButton.setEnabled(true);
//        mCancelButton.setEnabled(false);
    }


    /**
     * Cancel any process running plus any still queued
     */
    protected void doCancel() {
        setOkToProcess(false);
        executor.execute(new StopSerialExecuterRunnable());
    }

    /**
     * Set flag so subsequent queued executors will process
     */
    private class OkSerialExecutorRunnable implements Runnable {
        @Override
        public void run() {
            setOkToProcess(true);
        }
    }


    /**
     * Set process status
     * @param okToExecute
     */
    private synchronized void setOkToProcess(boolean okToExecute) {
        this.okToExecute = okToExecute;
    }

    /**
     * Run the serial process until it has either completed or canceled
     */
    private class StartSerialExecuterRunnable implements Runnable {
        private int processId;

        StartSerialExecuterRunnable(int processId) {
            this.processId = processId;
        }

        @Override
        public void run() {
            if (!okToExecute) {
                updateStatus("Process " + processId + " is cancelled"  );
                return;
            }

            updateStatus("Processing :" + processId);
            for (int p = 0; p <= 100; p += 25) {
                updateProgressBar(p);
                if (!okToExecute) {
                    updateStatus("Process " + processId +  "was cancelled "  );
                    break;
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        /**
         * Update status
         * @param status
         */
        private void updateStatus(final String status) {
            mTvStatusArea.post(new Runnable() {
                public void run() {
                    mTvStatusArea.append(status + sLineSeparator);
                }
            });
        }

        /**
         * Update progress bar for this process
         * @param progress
         */
        private void updateProgressBar(final int progress) {
            mProgressBar.post(new Runnable() {
                public void run() {
                    mProgressBar.setProgress(progress);
                }
            });
        }
    }

    /**
     * Used to block subsequent process requests until any prior ones
     * have completed (finished or canceled)
     */
    private class StopSerialExecuterRunnable implements Runnable {
        @Override
        public void run() {
            setOkToProcess(false);
        }
    }


}


