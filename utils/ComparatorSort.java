package xyz;

import java.util.*;
  
public class StandardSort {
    public static List<User> sort(List<User> toSort) {
        Map<User, Integer> map = new HashMap<>();
        for(User u : toSort)
            map.put(u, u.getLvl());
        TreeMap<User, Integer> sortedMap = new TreeMap<>(new ValueComparator(map));
        sortedMap.putAll(map);
        return new ArrayList<>(sortedMap.keySet());
    }

    private static class ValueComparator implements Comparator<User> {

        Map<User, Integer> base;

        ValueComparator(Map<User, Integer> base) {
            this.base = base;
        }

        @Override
        public int compare(User a, User b) {
            return base.get(a) < base.get(b) ? 1 : -1;
        }

    }
}
