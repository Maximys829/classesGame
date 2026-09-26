import java.util.*;

import static java.lang.Math.*;


public class Main{

    public static class Entity{
        private String name;
        private String type;
        private int maxHealth = 10;
        private int curHealth = maxHealth;
        private int attack = 0;
        private int attackR = 0;
        private int defense = 0;
        private int defenseR = 0;
        private int initiative = 0;
        private List<Status> statuses = new ArrayList() {};
        private HashMap<String, Double> resists = new HashMap<>();
        public void setMaxHealth(int maxHealth){
            this.maxHealth = maxHealth;
        }
        public void setMaxHealthAndHeal(int maxHealth){
            this.maxHealth = maxHealth;
            this.curHealth = maxHealth;
        }
        public int getMaxHealth(){
            return maxHealth;
        }
        public void decreaseHealth(int amount){
            curHealth -= amount;
            if(curHealth <= 0){
                curHealth = 0;
            }
        }
        public void increaseHealth(int amount){
            curHealth += amount;
            if(curHealth > maxHealth){
                curHealth = maxHealth;
            }
        }
        public int getCurHealth(){
            return curHealth;
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
            System.out.printf("%s %s:\nHealth: %d/%d, Def: %s\n", type,  name, curHealth,maxHealth, defenseR);
        }
        public String getType(){
            return type;
        }
        public void setType(String type){
            this.type = type;
        }
        public HashMap<String, Double> getResists(){
            return resists;
        }
        public void setResists(HashMap<String, Double> resists){
            this.resists = resists;
        }
        public void fillResists(List<Double> resists){
            this.resists.put("poison", resists.get(0));

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
        Poison(int poison){
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
    public static class Stun extends Status{
        Stun(int duration){
            setDuration(duration);
            this.setName("stun");
        }
        public int tick(){
            if(getDuration()>=1){
                tickDuration();
                return 1;
            }
            return -1;
        }
    }
    public static class Hero extends Entity{
        HashMap<String, Double> resists = new HashMap<String, Double>();
        private int actionCnt = 1;
        public void attack(Entity target){
            target.decreaseHealth(max(this.getAttackR()-target.getDefenseR(), 0));
        }
        public void block(){
            int n = 5;
            this.setStatus(new Block(3-1, n));
            setDefenseR(getDefenseR()+n);
        }
        public void triggerStatuses(){
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
                        decreaseHealth((int) round(max(t*getResists().get("poison"), 0)));
                        break;
                    case "stun":
                        this.actionCnt = 0;
                        break;
                }
            }
        }
        public void startTurn(){
            setDefenseR(getDefense());
            setAttackR(getAttack());
            this.triggerStatuses();
        }
    }
    public static class Archer extends Hero{
        Archer(String name){
            List<Double> list = List.of(0.8, 1.);
            this.fillResists(list);
            this.setMaxHealthAndHeal(90);
            this.setAttack(20);
            this.setDefense(0);
            this.setInitiative(5);
            this.setName(name);
            this.setType("Archer");
        }
        public void poisonAttack(Entity target){
            target.setStatus(new Poison(8+1));
        }
    }
    public static class Cleric extends Hero{
        private int healAmount;
        Cleric(String name){
            this.healAmount = 10;
            List<Double> list = List.of(1., 1.);
            this.fillResists(list);
            this.setMaxHealthAndHeal(100);
            this.setAttack(15);
            this.setDefense(0);
            this.setInitiative(0);
            this.setName(name);
            this.setType("Cleric");
        }
        public void heal(Entity target){
            target.setMaxHealthAndHeal(target.getMaxHealth()+healAmount);
        }
    }
    public static class Fighter extends Hero{
        Fighter(String name){
            List<Double> list = List.of(0.9, 1.);
            this.fillResists(list);
            this.setMaxHealthAndHeal(120);
            this.setAttack(25);
            this.setDefense(5);
            this.setInitiative(3);
            this.setName(name);
            this.setType("Fighter");
        }

        @Override
        public void block() {
            int n = 10;
            this.setStatus(new Block(3-1, n));
            setDefenseR(getDefenseR()+n);
        }
        public void stun(Entity target){
            target.setStatus(new Stun(1));
        }
    }
    public static class Wizard extends Hero {
        Wizard(String name) {
            List<Double> list = List.of(1.2, 1.);
            this.fillResists(list);
            this.setMaxHealthAndHeal(80);
            this.setAttack(15);
            this.setDefense(0);
            this.setInitiative(-3);
            this.setName(name);
            this.setType("Wizard");
        }
        public void magicMissile(Entity target){
            target.decreaseHealth(18);//ignores def
        }
    }



        public static void main(String[] args){
        Archer ar1 = new Archer("1");
        Archer ar2 = new Archer("2");
        Wizard w1 = new Wizard("1");
        Fighter f1 = new Fighter("1");
        ar2.startTurn();
        ar2.block();
        ar1.startTurn();
        ar1.poisonAttack(ar2);
        ar1.poisonAttack(ar2);
        ar2.startTurn();
        //ar2.startTurn();
        ar2.printStats();
        f1.block();
        w1.magicMissile(f1);
        f1.printStats();
    }
}