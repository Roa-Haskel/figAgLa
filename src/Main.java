import xyz.roahaskel.fight.Person;
import xyz.roahaskel.fight.Player;
import xyz.roahaskel.fight.User;

public class Main {
    static int round;
    public static void main(String[] args){
        init();
        Person.Licensing();
        Person p;
        while (true){
            p=Person.getNext();
            p.run();
            if(p.getHandsData()==0){
                return;
            }
        }
    }
    public static void init(){
        for(int i=0;i<2;i++){
            new Player();
        }
        new User();
        System.out.println("init users ok!!");
    }
}
