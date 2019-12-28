package xyz;

import java.util.*;

/*
    Użycie:
        Po prostu do obiektu, który ma być losowany, implementujesz interfejs Drawable
        Później losujesz to za pomocą statycznej metody draw(List<? extends Drawable> pool, Random random)
        Gdy suma szans nie osiągnie 1, wynik losowania będzie zbliżony do tego jakby szanse zostały przeskalowane tak, aby ich suma dała 1
    Już nie bede tego za każdym razem pisać

 */
public interface Drawable {

    /*
        Szanse w przedziale od 0 do 1
     */
    double getChance();

    default boolean draw(Random random) {
        return random.nextDouble() < getChance();
    }

    /*
        Uwaga: Kolejność elementów w Liście, danej jako argument, będzie stracona!
     */
    static Drawable draw(List<? extends Drawable> pool, Random random) {
        Collections.shuffle(pool);
        while(true)
            for(Drawable drawable : pool)
                if(drawable.draw(random))
                    return drawable;
    }

}
