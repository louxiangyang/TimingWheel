package com.louxy.Timer;

import java.util.concurrent.DelayQueue;

/**
 * @Auther: louxiangyang
 * @Date: 2020/6/30 10:04
 * @Description:
 */
public class TimingWheel {
    private final Long tickMs;
    private final Integer wheelSize;
    private final Long interval;
    private final DelayQueue<TimingTaskList> queue;
    private Long currentTime;
    private final TimingTaskList[] bukets;
    private TimingWheel overflowWheel;

    public TimingWheel(Long tickMs,Integer wheelSize,Long startMs,DelayQueue<TimingTaskList> queue){
        this.tickMs = tickMs;
        this.wheelSize = wheelSize;
        this.interval = tickMs * wheelSize;
        this.queue = queue;
        this.currentTime = startMs - (startMs % tickMs);
        bukets = new TimingTaskList[wheelSize];
        initBukets();
    }

    public void initBukets(){
        for (int i = 0; i < bukets.length; i++) {
               bukets[i] = new TimingTaskList();
        }
    }

    public void addOverflowWheel(){
        synchronized (this){
            if(overflowWheel==null){
                overflowWheel = new TimingWheel(interval,wheelSize,currentTime,queue);
            }
        }
    }

    public Boolean add(TimingTaskEntity entity){
        final Long expirationMs = entity.expirationMs;
        if(entity.cancel){
            return false;
        }else if(expirationMs<currentTime+tickMs){
            return false;
        }else if(expirationMs<currentTime+interval){
            final Long virtualId = expirationMs / tickMs;
            final Integer buketId = ((Long)(virtualId % wheelSize.longValue())).intValue();
            TimingTaskList buket = bukets[buketId];
            buket.add(entity);
            System.out.println(entity.toString()+"将插入"+this.toString()+"buketId="+buketId);
            if(buket.setExpiration(virtualId * tickMs)){
                queue.offer(buket);
            }
            return true;
        }else{
            if(overflowWheel==null)addOverflowWheel();
            return overflowWheel.add(entity);
        }
    }

    public void advanceClock(Long timeMs){
        if(timeMs >= currentTime+tickMs){
            currentTime = timeMs - (timeMs % tickMs);
            if(overflowWheel!=null)overflowWheel.advanceClock(currentTime);
        }
    }

    @Override
    public String toString() {
        return "时间轮:["+tickMs+"ms]";

    }
}
