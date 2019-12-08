package xyz;

import java.util.*;
  
public class ComparatorSort {
  
    /*
      Sortowanie malejące (według wartości User::getLevel)
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
            if(a.getLevel() < b.getLevel()){
                return 1;
            } else{
                if(a.getLevel() == b.getLevel()){
                    int compare = a.getName().compareTo(b.getName());

                    if (compare < 0 || compare == 0) {
                        return -1;
                    } else if (compare > 0) {
                        return 1;
                    }
                } else{
                    return -1;
                }
                return -1;
            }
        }
    }

}
