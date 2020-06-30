package com.louxy.Timer;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: louxiangyang
 * @Date: 2020/6/30 10:11
 * @Description:
 */
public class TimingTaskList implements Delayed,Iterable<TimingTaskEntity> {

    private final List<TimingTaskEntity> entities = new LinkedList<TimingTaskEntity>();
    Long expirationMs;

    public synchronized Boolean add(TimingTaskEntity entity){
        return entities.add(entity);
    }

    public   boolean setExpiration(Long expirationMs){
        if(this.expirationMs==null){
            this.expirationMs = expirationMs;
            return true;
        }
        if(this.expirationMs!=expirationMs){
            this.expirationMs = expirationMs;
            return false;
        }
        return false;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        Long delayTime = (expirationMs-System.currentTimeMillis())*1000000;
        return delayTime;
    }

    @Override
    public int compareTo(Delayed o) {
        return (((TimingTaskList)o).expirationMs)-this.expirationMs>0?-1:1;
    }

    @Override
    public Iterator<TimingTaskEntity> iterator() {
        return entities.iterator();
    }
}
