import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.max;
import static java.lang.Math.min;


public class Main{

    public static class Entity{
        private String name;
        private String type;
        private int health = 10;
        private int attack = 0;
        private int attackR = 0;
        private int defense = 0;
        private int defenseR = 0;
        private int initiative = 0;
        private List<Status> statuses = new ArrayList() {};
        private HashMap<String, Integer> resists = new HashMap<>();
        public void setHealth(int health){
            this.health = health;
        }
        public int getHealth(){
            return health;
        }
        public void setAttack(int attack){
            this.attack = attack;
        }
        public int getAttack(){
            return attack;
        }
        public void setDefense(int defense){
            this.defense = defense;
        }
        public int getDefense(){
            return defense;
        }
        public void setInitiative(int initiative){
            this.initiative = initiative;
        }
        public int getInitiative(){
            return initiative;
        }
        public void setStatus(Status status){
            if(!statuses.contains(status)){
                this.statuses.add(status);
            } else {
                statuses.get(statuses.indexOf(status)).add(status);

            }

        }
        public List<Status> getStatuses(){
            return statuses;
        }
        public void setDefenseR(int defenseR){
            this.defenseR = defenseR;
        }
        public int getDefenseR(){
            return defenseR;
        }
        public void setAttackR(int attackR){
            this.attackR = attackR;
        }
        public int getAttackR(){
            return attackR;
        }
        public void setName(String name){
            this.name = name;
        }
        public String getName(){
            return name;
        }
        public void printStats(){
            System.out.printf("%s %s:\nHealth: %d, Def: %s", type,  name, health, defenseR);
        }
        public String getType(){
            return type;
        }
        public void setType(String type){
            this.type = type;
        }
        public HashMap<String, Integer> getResists(){
            return resists;
        }
        public void setResists(HashMap<String, Integer> resists){
            this.resists = resists;
        }
    }
    public static class Status extends Entity{
        private int duration;
        public void setDuration(int duration){
            this.duration = duration;
        }
        public int getDuration(){
            return duration;
        }
        public void tickDuration(){
            duration--;
        }
        public int tick(){
            return -1;
        }
        public int getVal(){
            return 0;
        }
        public void add(Status status) {
        }
        @Override
        public boolean equals(Object obj){
            if(obj instanceof Status){
                if(Objects.equals(((Status) obj).getName(), this.getName())){
                    return true;
                }
            }
            return false;
        }
    }
    public static class Block extends Status{
        private int ddef;
        Block(int duration, int ddef){
            setDuration(duration);
            this.ddef = ddef;
            this.setName("block");
        }
        public int tick(){
            if(getDuration()>=1){
                tickDuration();
                return ddef;
            } else {
                return -1;
            }
        }
        public int getVal(){
            return ddef;
        }
        @Override
        public void add(Status block2){
            this.ddef += ((Block)block2).ddef;
            this.setDuration(this.getDuration()+1);
        }
    }
    public static class Poison extends Status{
        private int poison;
        Poison(int duration, int poison){
            setDuration(duration);
            this.poison = poison;
            this.setName("poison");
        }
        public int tick(){
            if(poison>=1){
                poison--;
                return poison;
            }
            return -1;
        }
        public int getVal(){
            return poison;
        }
        @Override
        public void add(Status poison2){
            poison+=((Poison) poison2).poison*2/3;
        }
    }
    public static class Hero extends Entity{
        public void attack(Entity target){
            target.setHealth(target.getHealth()-max(this.getAttackR()-target.getDefenseR(), 0));
        }
        public void block(){
            int n = 5;
            this.setStatus(new Block(3-1, n));
            setDefenseR(getDefenseR()+n);
        }
        public void triggerStatuses(){
            setDefenseR(getDefense());
            setAttackR(getAttack());
            for(Status s : this.getStatuses()){
                int t = s.tick();
                if(t==-1){
                    getStatuses().remove(s);
                    return;
                }
                switch (s.getName()){
                    case "block":
                        setDefenseR(getDefenseR()+t);
                        break;
                    case "poison":
                        setHealth(getHealth()-max(t-getResists().get("poison"), 0));
                        break;
                }
            }
        }
        public void startTurn(){
            this.triggerStatuses();
        }
    }
    public static class Archer extends Hero{
        Archer(String name){
            this.setResists(new HashMap<>());
            this.getResists().put("poison", 1);
            this.setHealth(90);
            this.setAttack(20);
            this.setDefense(0);
            this.setInitiative(5);
            this.setName(name);
            this.setType("Archer");
        }
        public void poisonAttack(Entity target){
            target.setStatus(new Poison(5, 8+1));
        }
    }



    public static void main(String[] args){
        Archer ar1 = new Archer("1");
        Archer ar2 = new Archer("2");
        ar2.startTurn();
        ar2.block();
        ar1.startTurn();
        ar1.poisonAttack(ar2);
        ar1.poisonAttack(ar2);
        ar2.startTurn();
        ar2.startTurn();
        ar2.printStats();
    }
}