package xyz.roahaskel.fight;
import sun.awt.image.ImageWatched;

import java.util.*;

public abstract class Person extends Brand{
    static int objNum=0,bossId,firstId,lastId,indexId;
    static Skill lastSk;
    public static ArrayList<Person> group=new ArrayList(3);
    ///////////////
    int playId;
    String name;
    public abstract void run();
    Person(){
        playId=objNum++;
        group.add(this);
    }
    public static void initRound(){
        lastSk=null;
    }
    public static void Licensing(){
        Person.shuffle();
        for(int i=0;i<group.size();i++){
            group.get(i).getCard(3+17*i,3+17*i+17);
        }
        group.get((int)(Math.random()*group.size())).asBoss();
        indexId=bossId;
    }
    public static Person getNext(){
        return group.get(indexId++%group.size());
    }
    public void asBoss(){
        bossId=playId;
        getCard(0,3);
    }
}



