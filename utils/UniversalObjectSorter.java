package xyz;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.collect.Lists;
  
public class UniversalObjectSorter {
    
    //toSort - Collection of objects containing getter returning int/double according to which list will be sorted
    //topToBottom - Order of returned values in list should be decreasing (true) or rising (false)
    //getterName - Name of method returning int/double according to which list will be sorted
    //getterArgs - If necessary arguments that method getterName requires
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static List<?> sortCollection(Collection toSort, boolean topToBottom, String getterName, Object... getterArgs) {
        Method getter = null;
        for(Method m : ((List<Object>) toSort).get(0).getClass().getMethods())
            if(m.getName().equals(getterName)) {
                getter = m;
                break;
            }
        try {
            if(getter.getReturnType() == Double.class || getter.getReturnType() == double.class) {
                HashMap<Object, Double> map = new HashMap<Object, Double>();
                DoubleValueComparator vc = new DoubleValueComparator(map);
                TreeMap<Object, Double> sorted_map = new TreeMap<Object, Double>(vc);
                for(Object u : toSort)
                    map.put(u, (Double) getter.invoke(u, getterArgs));
                sorted_map.putAll(map);
                if(topToBottom)
                    return Lists.newArrayList(sorted_map.keySet());
                return Lists.reverse(Lists.newArrayList(sorted_map.keySet()));
            } else if(getter.getReturnType() == Integer.class || getter.getReturnType() == int.class) {
                HashMap<Object, Integer> map = new HashMap<Object, Integer>();
                IntegerValueComparator vc = new IntegerValueComparator(map);
                TreeMap<Object, Integer> sorted_map = new TreeMap<Object, Integer>(vc);
                for(Object u : toSort)
                    map.put(u, (Integer) getter.invoke(u, getterArgs));
                sorted_map.putAll(map);
                if(topToBottom)
                    return Lists.newArrayList(sorted_map.keySet());
                return Lists.reverse(Lists.newArrayList(sorted_map.keySet()));
            }
        } catch(Exception ex) {}
        return null;
    }
    
    //toSort - Array of objects containing getter returning int/double according to which list will be sorted
    //topToBottom - Order of returned values in list should be decreasing (true) or rising (false)
    //getterName - Name of method returning int/double according to which list will be sorted
    //getterArgs - If necessary arguments that method getterName requires
    public static <T> List<?> sortArray(T[] toSort, boolean topToBottom, String getterName, Object... getterArgs) {
        Method getter = null;
        Object[] array = ((Object[])toSort);
        for(Method m : array[0].getClass().getMethods())
            if(m.getName().equals(getterName)) {
                getter = m;
                break;
            }
        try {
            if(getter.getReturnType() == Double.class || getter.getReturnType() == double.class) {
                HashMap<Object, Double> map = new HashMap<Object, Double>();
                DoubleValueComparator vc = new DoubleValueComparator(map);
                TreeMap<Object, Double> sorted_map = new TreeMap<Object, Double>(vc);
                for(Object u : array)
                    map.put(u, (Double) getter.invoke(u, getterArgs));
                sorted_map.putAll(map);
                if(topToBottom)
                    return Lists.newArrayList(sorted_map.keySet());
                return Lists.reverse(Lists.newArrayList(sorted_map.keySet()));
            } else if(getter.getReturnType() == Integer.class || getter.getReturnType() == int.class) {
                HashMap<Object, Integer> map = new HashMap<Object, Integer>();
                IntegerValueComparator vc = new IntegerValueComparator(map);
                TreeMap<Object, Integer> sorted_map = new TreeMap<Object, Integer>(vc);
                for(Object u : array)
                    map.put(u, (Integer) getter.invoke(u, getterArgs));
                sorted_map.putAll(map);
                if(topToBottom)
                    return Lists.newArrayList(sorted_map.keySet());
                return Lists.reverse(Lists.newArrayList(sorted_map.keySet()));
            }
        } catch(Exception ex) {}
        return null;
    }
    
    private static class DoubleValueComparator implements Comparator<Object> {
        Map<Object, Double> base;
        public DoubleValueComparator(Map<Object, Double> base) {
            this.base = base;
        }
        public int compare(Object a, Object b) {
            if (base.get(a) >= base.get(b))
                return -1;
            else
                return 1;
        }
    }
    
    private static class IntegerValueComparator implements Comparator<Object> {
        Map<Object, Integer> base;
        public IntegerValueComparator(Map<Object, Integer> base) {
            this.base = base;
        }
        public int compare(Object a, Object b) {
            if (base.get(a) >= base.get(b))
                return -1;
            else
                return 1;
        }
    }
}