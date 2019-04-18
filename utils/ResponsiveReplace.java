package xyz;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResponsiveReplace {

    //string - string in which certain items will be replaced
    //pattern - pattern with number - similar to "\\{TOP\\-[0-9]*\\}" - where number indicates place from list+1
    //list - list from which item will be taken
    public String replace(String string, String pattern, List<?> list) {
        Matcher m = Pattern.compile(pattern).matcher(string);
        while(m.find()) {
            String raw = m.group();
            String match = raw.substring(8);
            match = match.subSequence(0, match.indexOf("-")).toString();
            System.out.println(match);
            if(isInt(match)) {
                int index = Integer.valueOf(match)-1;
                if(list.size() > index) {
                    string = string.replaceFirst(Pattern.quote(raw), String.valueOf(list.get(Integer.valueOf(match)-1)));
                    continue;
                }
            }
            string = string.replaceFirst(Pattern.quote(raw), "NONE");
        }
        return string;
    }

    public boolean isInt(String s){
        try {
            Integer.valueOf(s);
            return true;
        } catch(Exception ex) {}
        return false;
    }
}
