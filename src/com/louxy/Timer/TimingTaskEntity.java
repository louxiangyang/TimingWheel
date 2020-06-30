package com.louxy.Timer;

/**
 * @Auther: louxiangyang
 * @Date: 2020/6/30 10:16
 * @Description:
 */
public class TimingTaskEntity implements  Runnable {

    protected Boolean cancel;
    protected Long expirationMs;
    protected Long interval;

    private String name;

    public TimingTaskEntity(String name,Long interval){
           this.expirationMs = System.currentTimeMillis()+interval;
           this.interval = interval;
           cancel = false;
           this.name = name;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+"-:"+this.toString());
    }

    @Override
    public String toString() {
        return "["+name+":预计过期时间:"+expirationMs+",当前时间"+System.currentTimeMillis()+"]";
    }
}
