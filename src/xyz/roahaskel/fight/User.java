package xyz.roahaskel.fight;

import java.util.*;

public class User extends Person {
    static List<String> mapp=new ArrayList(14);
    public User(){
        super();
        name="人脑"+playId;
    }
    static {
        for (String str : new String[]{"3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A", "2", "小鬼", "大鬼"}) {
            mapp.add(str);
        }
    }
    public Skill solve() {
        Scanner sc=new Scanner(System.in);
        Map<Integer,Integer> dit=new HashMap();
        int index,count;
        getHandsData();
        while (true){
            //键盘输入实例 3 3 3 5 三个三带一个五的输入
            dit.clear();
            String line=sc.nextLine();
            if(line.length()==0){
                return null;
            }
            try{
                //从输入的牌得到一个牌的值和数量对应关系的字典
                for(String str:line.split(" +")){
                    index=mapp.indexOf(str.toUpperCase());
                    if(index<0){
                        System.out.println(str);
                        throw new Exception("没有这样的牌面！");
                    }
                    if(dit.containsKey(index)){
                        dit.put(index,dit.get(index)+1);
                    }else{
                        dit.put(index,1);
                    }
                }
                //检查各数量能否维持要出的牌
                count=0;
                for(Map.Entry<Integer,Integer> item:dit.entrySet()){
                    if(handsData[item.getKey()]<item.getValue()){
                        System.out.println(handsData[item.getKey()]+" "+item.getValue());
                        System.out.println(dit);
                        throw new Exception("手里没有这样的牌");
                    }
                    count+=item.getValue();
                }
            }catch (Exception e){
                System.out.println(e);
                continue;
            }
            //检查符合哪种规则
            index=Collections.max(dit.values());
            Skill sk=new Skill();
            sk.max=Collections.max(dit.keySet());
            if(count==1){
                sk.sign=5;
            }else if(count==2 && index==2){
                sk.sign=0;
            }else if(index==2 && Collections.min(dit.values())==2 && isCoint(dit.keySet())){
                sk.sign=1;
            }else if(index==4 || (dit.containsKey(13) && dit.containsKey(14))){
                sk.sign=3;
            }else if(index==1 && count>=5 && isCoint(dit.keySet())){
                sk.sign=4;
            }else if(index==3){
                for(Map.Entry<Integer,Integer> item:dit.entrySet()){
                    if(item.getValue()<3){
                        dit.remove(item.getKey());
                    }
                    sk.max=Collections.max(dit.keySet());
                }
                if(isCoint(dit.keySet())){
                    sk.sign=3;
                }
            }
            if(sk.sign>=0){
                for(Map.Entry<Integer,Integer> ent:dit.entrySet()){
                    for(int i=0;i<ent.getValue();i++){
                        sk.append(ent.getKey());
                    }
                }
                if(
                        (       lastSk==null ||
                                sk.getSign()==lastSk.getSign() && sk.getSize()==lastSk.getSize() && sk.getMax()>lastSk.getMax() && sk.getSign()!=3)
                                || (sk.getSign()==3 && lastSk.getSign()!=3) || (lastId==playId)
                ) {
                    return sk;
                }else{
                    System.out.println("打不赢");
                }
            }else{
                System.out.println("不合法！");
            }
        }
    }
    private boolean isCoint(Collection<Integer> set){
        if(Collections.max(set)>11){
            return false;
        }
        List<Integer> ls=new ArrayList<>(set);
        Collections.sort(ls);
        int num=ls.get(0);
        for(int i=1;i<ls.size();i++){
            if(num+i!=ls.get(i) || ls.get(i)>11){
                return false;
            }
        }
        return true;
    }

    @Override
    public void run() {
        System.out.println();
        showAll();
        System.out.print(name+":");
        Skill sk=solve();
        if(sk==null){
            return;
        }
        lastSk=sk;
        lastId=playId;
        System.out.print(name+":");
        play(sk);
    }
}