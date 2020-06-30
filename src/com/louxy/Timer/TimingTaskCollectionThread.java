package com.louxy.Timer;

import java.util.Iterator;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Auther: louxiangyang
 * @Date: 2020/6/30 13:18
 * @Description:
 */
public class TimingTaskCollectionThread extends Thread {

    private final DelayQueue<TimingTaskList> queue;
    private final ThreadPoolExecutor threads;
    private final TimingWheel wheel;

    public TimingTaskCollectionThread(TimingWheel wheel, DelayQueue<TimingTaskList> queue, ThreadPoolExecutor threads){
        this.queue = queue;
        this.threads = threads;
        this.wheel = wheel;
    }

    @Override
    public void run() {
       while (true){
           try {
               TimingTaskList timingTaskList = queue.take();
               Long expirationMs = timingTaskList.expirationMs;
               wheel.advanceClock(expirationMs);
               synchronized (timingTaskList){
                    Iterator<TimingTaskEntity> it = timingTaskList.iterator();
                    while (it.hasNext()){
                        TimingTaskEntity entity = it.next();
                        it.remove();
                        if(!wheel.add(entity)&&!entity.cancel){
                            threads.execute(entity);
                        }
                    }
                   timingTaskList.setExpiration(null);
               }
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }
    }
}
