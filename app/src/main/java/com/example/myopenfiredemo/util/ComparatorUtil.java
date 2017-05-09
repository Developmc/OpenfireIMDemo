package com.example.myopenfiredemo.util;

import com.example.myopenfiredemo.model.SearchPerson;

import java.util.Comparator;

/**ComparatorUtil 排序工具类
 * Created by clement on 2017/4/24.
 */

public class ComparatorUtil {

    /**
     * 正序排列：从小到大
     * @return
     */
    public static Comparator<SearchPerson> searchPersonComparator(){
        return new Comparator<SearchPerson>() {
            @Override
            public int compare(SearchPerson o1, SearchPerson o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        };
    }
}
