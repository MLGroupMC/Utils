package xyz;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.collect.Lists;
  
public class StandartSort {
    
    public static List<User> sort(List<User> toSort) { 
        HashMap<User, Integer> map = new HashMap<User, Integer>();
        ValueComparator bvc = new ValueComparator(map);
        TreeMap<User, Integer> sorted_map = new TreeMap<User, Integer>(bvc);
        for(User u : toSort) {
            map.put(u, u.getLvl());
        }
        sorted_map.putAll(map);
        return Lists.newArrayList(sorted_map.keySet());
    }
    
}

class ValueComparator implements Comparator<User> {
    Map<User, Integer> base;

    public ValueComparator(Map<User, Integer> base) {
        this.base = base;
    }
    
    public int compare(User a, User b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } 
    }
}
