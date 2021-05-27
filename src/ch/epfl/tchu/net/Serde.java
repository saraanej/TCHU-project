package ch.epfl.tchu.net;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * The Serde interface of the ch.epfl.tchu.net package is generic.
 * Represents an object capable of serializing and deserializing of a given type.
 * Contains four static construction methods.
 * @param <C> the type of elements that the serde is able to (de) serialize.
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */
public interface Serde<C> {

    /**
     * Generic method that constructs a Serde taking as arguments a serialization function and a deserialization function.
     * @param serialization the serialization function.
     * @param deserialization the deserialization function.
     * @param <T> the type of elements that the serde is able to (de) serialize.
     * @return the corresponding Serde<T>.
     *
     * @throws IllegalArgumentException if the serialisation and deserialization functions are null.
     */
    static <T> Serde<T> of(Function<T, String> serialization, Function<String, T> deserialization){
        Preconditions.checkArgument(serialization != null);
        Preconditions.checkArgument(deserialization != null);
        return new Serde<>() {
            @Override
            public String serialize(T t){
               return  serialization.apply(t);
            }
            @Override
            public T deserialize(String str){
                return deserialization.apply(str);
            }
        };
    }

    /**
     * Generic method that constructs a Serde out of the list of all the values of an enumerated set.
     * @param values the list of all the values of the enumerated set.
     * @param <T> the type of elements that the serde is able to (de)serialize.
     * @return the corresponding Serde<T>.
     *
     * @throws IllegalArgumentException if list of values is empty.
     */
    static <T> Serde<T> oneOf(List<T> values){
        Preconditions.checkArgument(!values.isEmpty());
        return new Serde<>() {
            @Override
            public String serialize(T t){
                Preconditions.checkArgument(values.contains(t));
                return Integer.toString(values.indexOf(t));
            }
            @Override
            public T deserialize(String str){
                T value = values.get(Integer.parseInt(str));
                Preconditions.checkArgument(values.contains(value));
                return value;
            }
        };
    }

    /**
     * Generic method that constructs a Serde able to (de)serialize lists of values (de)serialized by a given serde.
     * @param serde the serde to use to (de)serialize the elements of the list.
     * @param separator the separator to use to separate the serialized's list's elements.
     * @param <T> the type of elements that the serde is able to (de)serialize.
     * @return the corresponding Serde<List<T>>.
     *
     * @throws IllegalArgumentException if the separator is nul or an empty String,
     *                                  if the serde is null.
     */
    static <T> Serde<List<T>> listOf(Serde<T> serde, String separator){
        Preconditions.checkArgument(separator != null && separator != "");
        Preconditions.checkArgument(serde != null);
        return new Serde<>() {

            /**
             * @throws IllegalArgumentException if the list to serialize is null.
             */
            @Override
            public String serialize(List<T> t){
                Preconditions.checkArgument(t != null);
                List<String> serialized = new ArrayList<>();
                if(t.isEmpty()) return "";
                for (T s : t){
                    serialized.add(serde.serialize(s));}
                return String.join(separator,
                        serialized);
            }
            @Override
            public List<T> deserialize(String str){
                if(str == null || str.isEmpty()) return List.of();
                String[] split = str.split(Pattern.quote(separator), -1);
                List<T> deserialized = new ArrayList<>();
                for (String s: split)
                    deserialized.add(serde.deserialize(s));
                return deserialized;}
        };
    }

    /**
     * Generic method that constructs a Serde able to (de)serialize SortedBags of values (de)serialized by a given serde.
     * @param serde the serde to use to (de)serialize the elements of the bag.
     * @param separator the separator to use to separate the serialized's bag's elements.
     * @param <T> the type of elements that the serde is able to (de)serialize.
     * @return the corresponding Serde<SortedBag<T>>.
     *
     * @throws IllegalArgumentException if the separator is nul or an empty String,
     *                                  if the serde is null.
     */
    static <T extends  Comparable<T>> Serde<SortedBag<T>> bagOf(Serde<T> serde, String separator){
        Preconditions.checkArgument(separator != null && separator != "");
        Preconditions.checkArgument(serde != null);
        return new Serde<>() {
            Serde<List<T>> serdeList = Serde.listOf(serde, separator);
            @Override
            public String serialize(SortedBag<T> t){
                List<T> list = t.toList();
                return serdeList.serialize(list);
            }
            @Override
            public SortedBag<T> deserialize(String str){
                return SortedBag.of(serdeList.deserialize(str));
            }
        };
    }

    /**
     * Abstract serialization method.
     * @param c the object to serialize.
     * @return the matching string serialization.
     */
    String serialize(C c);

    /**
     * Abstract deserialization method.
     * @param str the string to deserialize.
     * @return the corresponding deserialized object.
     */
    C deserialize(String str);
}
