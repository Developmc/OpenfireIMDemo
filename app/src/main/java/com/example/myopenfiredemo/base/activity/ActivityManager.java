package com.example.myopenfiredemo.base.activity;

import java.util.HashMap;
import java.util.Set;

/**activity管理器
 * Created by clement on 2017/4/17
 */

public class ActivityManager {
    private static ActivityManager activityTaskManager = null;
    private HashMap<String , BaseActivity> activityMap = null;
    private ActivityManager(){
        activityMap = new HashMap<>();
    }

    /**单例模式
     * @return
     */
    public static ActivityManager getInstance(){
        if(activityTaskManager==null){
            synchronized(ActivityManager.class) {
                if(activityTaskManager==null){
                    activityTaskManager = new ActivityManager();
                }
            }
        }
        return activityTaskManager;
    }

    /**将一个activity添加进管理器
     * @param name
     * @param activity
     * @return
     */
    public BaseActivity putActivity(String name,BaseActivity activity){
        return activityMap.put(name,activity);
    }

    /**根据名字获得activity
     * @param name
     * @return
     */
    public BaseActivity getActivity(String name){
        return activityMap.get(name);
    }

    /**判断是否为空
     * @return
     */
    public boolean isEmpty(){
        return activityMap.isEmpty();
    }

    /**获得管理器中activity的个数
     * @return
     */
    public int getSize(){
        return activityMap.size();
    }

    /**判断管理中是否包含指定name的activity
     * @param name
     * @return
     */
    public boolean containsName(String name){
        return activityMap.containsKey(name);
    }

    /**判断是否有指定的activity
     * @param activity
     * @return
     */
    public boolean containsActivity(BaseActivity activity){
        return activityMap.containsValue(activity);
    }

    /**finish activity
     * @param activity
     */
    private void finishActivity(BaseActivity activity){
        if(activity!=null && !activity.isFinishing()){
            activity.finish();
        }
    }

    /**移除activity,只是从map集合中移除
     * @param name
     * @param name
     */
    public void removeActivity(String name){
        if(activityMap != null && !activityMap.isEmpty()){
            activityMap.remove(name);
        }
    }

    /**
     * 关闭所有的activity
     */
    public void closeAllActivity(){
        Set<String> activityNames = activityMap.keySet();
        for(String name:activityNames){
            finishActivity(activityMap.get(name));
        }
        activityMap.clear();
    }

    /** 除了指定的activity,关闭其它所有的activity
     * @param nameSpecified
     */
    public void closeAllActivityExceptOne(String nameSpecified){
        Set<String> activityNames = activityMap.keySet();
        BaseActivity activitySpecified = activityMap.get(nameSpecified);
        for(String name:activityNames){
            if(!name.equals(nameSpecified)){
                finishActivity(activityMap.get(name));
            }
        }
        activityMap.clear();
        activityMap.put(nameSpecified,activitySpecified);
    }
}
