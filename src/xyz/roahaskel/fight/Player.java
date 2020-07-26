package xyz.roahaskel.fight;


import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.util.Arrays;

public class Player extends Person{
    public Player(){
        super();
        name="电脑"+playId;
    }
    public Skill solve() {
        update();
        int index;
        if(lastSk==null || lastId == playId){
            for(int i=0;i<500;i++){
                index=(int)(Math.random()*(skills.length-1));
                if(skills[index].size()>0 && index!=3 ){
                    if(skills[index].getFirst().getMax()<8 || skills[index].getFirst().getMax()<(int)(Math.random()*6-3+7)){
                        return skills[index].getFirst();
                    }
                }
            }
            for(int i=0;i<skills.length;i++){
                if(skills[i].size()>0){
                    return skills[skills.length-1].getFirst();
                }
            }
        }
        index=lastSk.getSign();
        for(int i=0;i<skills[index].size();i++){
            if(skills[index].get(i).getMax()>lastSk.getMax()){
//                skills[index].get(i).subSkill(0,lastSk);
                if(skills[index].get(i).getMax()-lastSk.getMax()<Math.random()*5){
                    return skills[index].get(i);
                }else{
                    break;
                }
            }
        }
        if(lastSk.getSign()!=3){
            return null;
        }
        if(skills[3].size()>0){
            return skills[3].getFirst();
        }
        return null;
    }

    @Override
    public void run() {
        Skill sk=solve();
        if(sk==null){
            System.out.println(name+"：要不起！");
            return;
        }
        if(lastId!=playId && lastSk!=null){
            sk.subSkill(0,lastSk);
        }
        System.out.print(name+":");
        play(sk);
        lastId=playId;
        lastSk=sk;
        int count=getHandsData();
        if(count==1){
            System.out.println("我只剩一张牌了！");
        }else if(count==2){
            System.out.println("报双");
        }
    }
}