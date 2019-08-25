package xyz;

import java.util.*;
  
public class StandardSort {
  
    /*
      Sort top to bottom
    */
    public static List<User> sort(List<User> toSort) {
        TreeSet<User> treeSet = new TreeSet<>(new ValueComparator());
        treeSet.addAll(toSort);
        return new ArrayList<>(treeSet);
    }

    private static class ValueComparator implements Comparator<User> {

        ValueComparator() {}

        @Override
        public int compare(User a, User b) {
            return a.getLvl() < b.getLvl() ? 1 : -1;
        }

    }

}
