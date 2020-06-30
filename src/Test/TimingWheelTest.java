package Test;

import com.louxy.Timer.TimingTaskCollectionThread;
import com.louxy.Timer.TimingTaskEntity;
import com.louxy.Timer.TimingTaskList;
import com.louxy.Timer.TimingWheel;

import java.util.concurrent.*;

/**
 * @Auther: louxiangyang
 * @Date: 2020/6/30 10:51
 * @Description:
 */
public class TimingWheelTest {
    public static void main(String[] args) {
        final  DelayQueue<TimingTaskList> queue = new DelayQueue<TimingTaskList>();
        TimingWheel wheel = new TimingWheel(1l,20,System.currentTimeMillis(),queue);
        wheel.add(new TimingTaskEntity("task-1",15l));
        wheel.add(new TimingTaskEntity("task-2",20l));
        wheel.add(new TimingTaskEntity("task-3",400l));
        wheel.add(new TimingTaskEntity("task-4",7000l));
        wheel.add(new TimingTaskEntity("task-5",8000l));

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10,20,10, TimeUnit.SECONDS, new LinkedBlockingQueue());
        TimingTaskCollectionThread timingTaskCollectionThread = new TimingTaskCollectionThread(wheel,queue,threadPoolExecutor);
        timingTaskCollectionThread.start();


    }
}
