package ch.epfl.tchu.net;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

public interface Serde<C> {

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
     * Creates a Serde out of a list of enumerated values
     * @param values
     * @param <T>
     * @return
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

    static <T> Serde<List<T>> listOf(Serde<T> serde, String separator){
        Preconditions.checkArgument(separator != null && separator != "");
        Preconditions.checkArgument(serde != null);
        return new Serde<>() {
            @Override
            public String serialize(List<T> t){
                List<String> serialized = new ArrayList<>();
                for (T s : t)
                    serialized.add(serde.serialize(s));
                return String.join(separator,
                        serialized);
            }
            @Override
            public List<T> deserialize(String str){
                String[] stringList = str.split(Pattern.quote(separator), -1);
                List<T> deserialized = new ArrayList<>();
                for (String s: stringList)
                    deserialized.add(serde.deserialize(s));
                return deserialized;
            }
        };
    }

    static <T extends  Comparable<T>> Serde<SortedBag<T>> bagOf(Serde<T> serde, String separator){
        Preconditions.checkArgument(separator != null && separator != "");
        Preconditions.checkArgument(serde != null);
        Serde<List<T>> serdeList = Serde.listOf(serde, separator);
        return new Serde<>() {
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

    String serialize(C c);

    C deserialize(String str);
}
