package xyz;

import java.util.*;

/*

    Ułatwienie do parsowania wiadomości z configów ze zmiennymi
    Instrukcja:
        Message(String rawPattern, String... sequence)

        rawPattern - wzór prosto z configu
        sequence - kolejność zmiennych jakie będą użyte w metodzie create(Object... args)

        Aby sparsować wiadomość należy użyć metody create(Object... args) i wprowadzić do niej zmienne,
        zgodnie z kolejnością zmiennych z konstruktora, tak aby mogły być odpowiednio podmieniane
        lower/upperCase w nazwach zmiennych ma znaczenie!!

        Format zmiennych:
            dla pojedynczej zmiennej:   {VARNAME}
            dla elementu %pos% z listy: {VARNAME-%pos%}
            dla zawartości listy:       {VARNAME}

        Przykład 1:
            Player player = ...
            List<Player> queueList = ...
            queueList.add(player);
            String queueMsg = "{PLAYER} dolaczyl do kolejki, wszyscy: {ALL}";
            Message message = new Message(queueMsg, "ALL", "PLAYER");
            player.sendMessage(message.create(queueList, player.getDisplayName()));

            Zauważ że kolejność "ALL", "PLAYER" odpowiada queueList, player.getDisplayName()

        Przykład 2:
            Player player = ...
            List<Player> topPlayers = ...
            String topMsg = "Top 3: {TOPKA-3}, Top 2: {TOPKA-2}, Top 1: {TOPKA-1}";
            Message message = new Message(topMsg, "TOPKA");
            player.sendMessage(message.create(topPlayers));

            Zauważ że lista nie zaczyna się od 0 tylko od 1 (ludzkie liczenie); zmienna topPlayers może być tablicą lub Collection

 */

public class Message {

    private final List<String> vars, messages, possibleDeclarations, varSequence;

    public Message(String rawPattern, String... sequence) {
        this(rawPattern);
        defineVariableSequence(sequence);
    }

    public Message(String rawPattern) {
        this.vars = new ArrayList<>();
        this.messages = new ArrayList<>();
        this.possibleDeclarations = new ArrayList<>();
        this.varSequence = new ArrayList<>();
        int count = 0, start = 0, last = -1;
        for(int i = 0; i < rawPattern.length(); i++) {
            if(rawPattern.charAt(i) == '{') {
                if(i != 0 && rawPattern.charAt(i - 1) == '\\')
                    rawPattern = rawPattern.substring(0, i-1)+rawPattern.substring(i);
                else {
                    count++;
                    if(count == 1) {
                        messages.add(rawPattern.substring(last + 1, i));
                        start = i;
                    }
                }
            } else if(rawPattern.charAt(i) == '}') {
                if(i != 0 && rawPattern.charAt(i - 1) == '\\')
                    rawPattern = rawPattern.substring(0, i-1)+rawPattern.substring(i);
                else {
                    count--;
                    if(count == 0) {
                        String var = rawPattern.substring(start + 1, i);
                        vars.add(var);
                        Object[] compounds = getArrayCompounds(var);
                        possibleDeclarations.add(((compounds[0] + "-" + compounds[1]).equals(var)) ? (String) compounds[0] : var);
                        last = i;
                    }
                }
            }
        }
        messages.add(rawPattern.substring(last + 1));
    }

    public void defineVariableSequence(String... sequence) {
        if(!varSequence.isEmpty())
            varSequence.clear();
        varSequence.addAll(Arrays.asList(sequence));
    }

    public String create(Object... args) {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < messages.size(); i++) {
            builder.append(messages.get(i));
            if(i < vars.size()) {
                String var = vars.get(i), declared = possibleDeclarations.get(i);
                int position = varSequence.indexOf(declared);
                if(position == -1) {
                    builder.append('{').append(var).append('}');
                    continue;
                }
                if(position >= args.length) {
                    builder.append("null");
                    continue;
                }
                Object data = args[position];
                if(data == null) {
                    builder.append("null");
                    continue;
                }
                if(var.equals(declared)) {
                    if(data.getClass().isArray()) {
                        Object[] array = (Object[]) data;
                        for(int j = 0; j < array.length;) {
                            builder.append(array[j]);
                            j++;
                            if(j != array.length)
                                builder.append(", ");
                        }
                    } else if(data instanceof Collection) {
                        List list = (data instanceof List) ? (List) data : new ArrayList((Collection) data);
                        for(int j = 0; j < list.size();) {
                            builder.append(list.get(j));
                            j++;
                            if(j != list.size())
                                builder.append(", ");
                        }
                    } else
                        builder.append(data);
                    continue;
                }
                builder.append(getFromAnyList(data, (int) getArrayCompounds(var)[1] - 1));
            }
        }
        return builder.toString();
    }

    private Object getFromAnyList(Object anyList, int index) {
        if(anyList == null || index < 0)
            return null;
        if(anyList.getClass().isArray()) {
            Object[] array = (Object[]) anyList;
            if(index < array.length)
                return ((Object[]) anyList)[index];
            return null;
        } else if(anyList instanceof Collection) {
            List list = (anyList instanceof List) ? (List) anyList : new ArrayList((Collection) anyList);
            if(index < list.size())
                return list.get(index);
            return null;
        }
        return anyList;
    }

    private Object[] getArrayCompounds(String array) {
        int pos = 0;
        for(int i = array.length() - 1; i > 0; i--)
            if(array.charAt(i) == '-') {
                try {
                    pos = Integer.valueOf(array.substring(i + 1));
                } catch(IllegalArgumentException ignored) {
                }
                return new Object[]{array.substring(0, i), pos};
            }
        return new Object[]{array, 0};
    }

}
