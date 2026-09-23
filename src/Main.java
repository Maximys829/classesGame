import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
            this.statuses.add(status);
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
            System.out.printf("%s %s:\nHealth:%d", type,  name, health);
        }
        public String getType(){
            return type;
        }
        public void setType(String type){
            this.type = type;
        }

    }
    public static class Status extends Entity{
        public int tick(){
            return -1;
        }
        public int getVal(){
            return 0;
        }
    }
    public static class Block extends Status{
        private int duration;
        private int ddef;

        Block(int duration, int ddef){
            this.duration = duration;
            this.ddef = ddef;
            this.setName("block");
        }
        public int tick(){
            if(duration>=1){
                duration -= 1;
                return ddef;
            } else {
                return -1;
            }
        }
        public int getVal(){
            return ddef;
        }
    }
    public static class Hero extends Entity{
        public void attack(Entity target){
            target.setHealth(target.getHealth()-max(this.getAttackR()-target.getDefenseR(), 0));
        }
        public void block(){
            this.setStatus(new Block(3, 5));
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
                }
            }
        }
        public void startTurn(){
            this.triggerStatuses();
        }
    }
    public static class Archer extends Hero{
        Archer(String name){
            this.setHealth(90);
            this.setAttack(20);
            this.setDefense(0);
            this.setInitiative(5);
            this.setName(name);
            this.setType("Archer");
        }

    }



    public static void main(String[] args){
        Archer ar1 = new Archer("1");
        Archer ar2 = new Archer("2");
        ar1.startTurn();
        ar2.startTurn();
        ar2.block();
        ar2.startTurn();
        ar1.attack(ar2);
        ar2.printStats();
    }
}