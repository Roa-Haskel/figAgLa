package xyz.roahaskel.fight;
import java.util.*;
public class Brand{
    //初始化一副扑克
    static List<Paper> papers=new ArrayList<>();
    static {
        for(int i=0;i<13;i++){
            for(int j=1;j<5;j++){
                papers.add(new Paper(i,j));
            }
        }
        papers.add(new Paper(13,0));
        papers.add(new Paper(14,5));
    }
    //一手牌的集合
    LinkedList<Integer>[] hands=new LinkedList[17];
    int[] handsData=new int[hands.length];
    //存放所有出牌组合的集合  {对子,连对,三带一和飞机,炸弹,联牌,单牌}
    LinkedList<Skill>[] skills=new LinkedList[6];
    public static void shuffle(){
        Collections.shuffle(papers);
    }
    public static void showPaper(int num,String color){

    }
    Brand(){
        for(int i=0;i<skills.length;i++){
            skills[i]=new LinkedList<Skill>();
        }
        for(int i=0;i<hands.length;i++){
            hands[i]=new LinkedList<Integer>();
        }
    }
    //显示手牌
    public void showAll(){
        List<Integer> ls;
        for(int i=0;i<hands.length;i++){
            ls=hands[i];
            for(int j=0;j<ls.size();j++){
                Paper.showPaper(i,ls.get(j));
            }
        }
        System.out.println();
    }
    //参数例子
    //[0,0]对三
    //[2,1,1,1] 三个四，一个五
    public void play(Skill sk){
        List<Integer> ls=new ArrayList();
        for(int i=0;i<sk.getSize();i++){
            Paper.showPaper(sk.get(i),hands[sk.get(i)].pop());
        }
        System.out.println();
    }
    public void getCard(int a,int b){
        Paper p;
        for(int i=a;i<b;i++){
            p=papers.get(i);
            hands[p.number].add(p.color);
        }
    }
    public int getHandsData(){
        int count=0;
        for(int i=0;i<handsData.length;i++){
            handsData[i]=hands[i].size();
            count+=handsData[i];
        }
        return count;
    }
    public void update(){
        for(int i=0;i<skills.length;i++){
            skills[i].clear();
        }
        LinkedList<Integer> stack=new LinkedList();
        getHandsData();
        //获取各种牌型
        //所有单牌组合
        getSigle();
        //所有对子组合
        getPair();
        //所有连对
        get234(stack,1,2,3);
        //所有三票
        get31(stack);
        //所有炸弹
        get234(stack,3,4,1);
        if(handsData[13]+handsData[14]>1){
            Skill sk=new Skill();
            sk.sign=3;
            sk.max=14;
            skills[4].add(sk);
        }
        //所有联牌
        get234(stack,4,1,5);

    }
    private void getPair(){
        //所有能出的对子
        for(int i=0;i<handsData.length;i++){
            if(handsData[i]>=2){
                Skill sk=new Skill();
                sk.append(i);
                sk.append(i);
                sk.perfect+=handsData[i]-2;
                sk.sign=0;
                skills[0].add(sk);
            }
        }
    }
    public void get31(LinkedList<Integer> stack){
       get234(stack,2,3,1);
       //增加单牌
    }
    public void getSigle(){
        for(int i=0;i<handsData.length;i++){
            if(handsData[i]>0){
                Skill sk=new Skill();
                sk.perfect+=handsData[i]-1;
                sk.append(i);
                sk.sign=5;
                skills[5].add(sk);
            }
        }
    }
    //获取2、3、4张牌组合的通用方法
    private void get234(LinkedList<Integer> stack,int num,int count,int minTimes){
        stack.clear();
        Skill sk=new Skill();
        for(int i=0;i<(minTimes==1?handsData.length:handsData.length-3);i++){
            if(handsData[i]>=count && ((i<handsData.length-3 && stack.size()>0) || stack.size()==0)){
                stack.push(i);
                sk.perfect+=handsData[i]-count;
            }else if(stack.size()>=minTimes){
                for(int j=0;j<stack.size();j++){
                    for(int k=0;k<count;k++){
                        sk.append(stack.get(j));
                    }
                    sk.sign=num;
                }
                stack.clear();
                sk.max=i-1;
                skills[num].add(sk);
                sk=new Skill();
            }else{
                stack.clear();
            }
        }
    }

    public void skillShow(){
        System.out.println(Arrays.toString(skills));
    }
}

class Paper{
    private static String[] numMapp={"3","4","5","6","7","8","9","10","J","Q","K","A","2","小鬼","大鬼"};
    private static String[] colorMapp=new String[]{"","♣","♠","♥","♦",""};
    int number;
    int color;
    Paper(int n,int c){
        number=n;
        color=c;
    }
    public static void showPaper(int num,int color){
        if (color >= 3) {
            System.out.print("\033[31;4m"+colorMapp[color] + numMapp[num]+"\033[0m");
//            System.out.println("\033[31;4m" + "Hello,Akina!" + "\033[0m");
        } else {
            System.out.print(colorMapp[color] + numMapp[num]);
        }
        System.out.print(" \t ");
    }
}

class Skill implements Comparable<Skill>{
    /*
    0 对子
    1 连对
    2 三张
    3 炸弹
    4 顺子
    5 单牌
     */
    private LinkedList<Integer> data=new LinkedList();
    public int perfect=0,max,sign;
    Skill(){
        sign=-1;
        max=-1;
    }
    public void append(Integer i){
        data.add(i);
    }
    public void pop(){
        data.pop();
    }
    public void clear(){
        data.clear();
    }
    public Integer get(int index){
        return data.get(index);
    }
    public void init(){
        perfect=0;
        data.clear();
    }
    public int getSize(){
        return data.size();
    }
    public int getMax(){
        return Collections.max(data);
    }
    public int getSign(){
        if(sign<0 || sign>5){
            if(getSize()==1){
                sign=5;
            }else if(getSize()==2 && new HashSet(data).size()==1){
                sign=0;
            }else if(getSize()==2 && new HashSet(data).size()==2){
                sign=3;
            }else if(Collections.frequency(data,getMax())==3){
                sign=2;
            }else if(getSize()>=6 && Collections.frequency(data,getMax())==2){
                sign=1;
            }else if(Collections.frequency(data,getMax())==4){
                sign=3;
            }else if(getSize()>=5 && Collections.frequency(data,getMax())==1){
                sign=4;
            }else{
                sign=-1;
            }
        }
        return sign;
    }
    //截取组合，能匹配参数组合的长度，并且大过他
    public void subSkill(int index,Skill sk){
        Collections.sort(data);
        if(getSign()==3 && sk.getSign()!=3){
            if(sk.getSize()>4){
                data=new LinkedList<>(data.subList(0,4));
                return;
            }
        }
        if(index<0){
            index=0;
        }
        int temp=getSize()-sk.getSize(),steep;
        if(temp==0){
            return;
        }
        if(sk.getSign()>=1 && sk.getSign()<4){
            steep=sk.getSign()+1;
        }else if(sk.getSign()==4){
            steep=1;
        }else{
            return;
        }
        int inx=(getMax()-sk.getMax()-1)*steep>temp?temp:(getMax()-sk.getMax()-1)*steep;
        int i=inx>index*steep?index*steep:inx;
        data=new LinkedList<>(data.subList(data.size()-sk.getSize()-inx+i,data.size()-inx+i));
    }
    @Override
    public String toString() {
        return "Skill{" +
                "data=" + data.toString() +
                ", perfect=" + perfect +
                ",max="+getMax()+
                ",sign="+getSign()+
                '}';
    }
    @Override
    public int compareTo(Skill o) {
        if(o==null){
            return getMax()+1;
        }else if(o.getSign()==getSign()){
            return getMax()-o.getMax();
        }else if(getSign()==3){
            return 10;
        }else{
            return -1;
        }
    }
}