package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;

import java.util.List;
import java.util.function.Function;

public interface Serde<C> {

    static <T> Serde<T> of(Function<T, String> serialize, Function<String, T> deserialize){
        return null;
    }

    /**
     * Creates a Serde out of a list of enumerated values
     * @param values
     * @param <T>
     * @return
     */
    static <T> Serde<T> oneOf(List<T> values){
        return null;
    }

    static <T> Serde<List<T>> listOf(Serde<T> serde, String separator){
        return null;
    }

    static <T extends  Comparable<T>> Serde<SortedBag<T>> bagOf(Serde<T> serde, String separator){
        return null;
    }


    String serialize(C object);

    C deserialize(String str);
}
