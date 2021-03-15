package ch.epfl.tchu.game;



public class TestStationPartition {

    public static void main(String args[]){

        int stations[] = new int[]{2,3,5,3,2,5,7,7};

        int representative = stations[3];
        int index = 3;

        if(index == representative){

            System.out.println(representative);

        } else {

            do{
                index = representative;
                representative = stations[index];

            } while(representative != index);

            System.out.println(representative);
        }

    }
}



