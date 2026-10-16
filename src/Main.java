import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static java.lang.Math.*;


public class Main {
    public static class Obj{
        private String name;
        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
    public static class Entity extends Obj{

        private String type;
        private int maxHealth = 10;
        private int curHealth = maxHealth;
        private int attack = 0;
        private int attackR = 0;
        private int defense = 0;
        private int defenseR = 0;
        private int initiative = 0;
        private List<Status> statuses = new ArrayList() {
        };
        private HashMap<String, Double> resists = new HashMap<>();

        public void setMaxHealth(int maxHealth) {
            this.maxHealth = maxHealth;
        }

        public void setMaxHealthAndHeal(int maxHealth) {
            this.maxHealth = maxHealth;
            this.curHealth = maxHealth;
        }

        public int getMaxHealth() {
            return maxHealth;
        }

        public void decreaseHealth(int amount) {
            curHealth -= amount;
            if (curHealth <= 0) {
                curHealth = 0;
            }
        }

        public void increaseHealth(int amount) {
            curHealth += amount;
            if (curHealth > maxHealth) {
                curHealth = maxHealth;
            }
        }

        public int getCurHealth() {
            return curHealth;
        }

        public void setAttack(int attack) {
            this.attack = attack;
        }

        public int getAttack() {
            return attack;
        }

        public void setDefense(int defense) {
            this.defense = defense;
        }

        public int getDefense() {
            return defense;
        }

        public void setInitiative(int initiative) {
            this.initiative = initiative;
        }

        public int getInitiative() {
            return initiative;
        }

        public void setStatus(Status status) {
            if (!statuses.contains(status)) {
                this.statuses.add(status);
            } else {
                statuses.get(statuses.indexOf(status)).add(status);

            }

        }

        public List<Status> getStatuses() {
            return statuses;
        }

        public void setDefenseR(int defenseR) {
            this.defenseR = defenseR;
        }

        public int getDefenseR() {
            return defenseR;
        }

        public void setAttackR(int attackR) {
            this.attackR = attackR;
        }

        public int getAttackR() {
            return attackR;
        }



        public void printStats() {
            System.out.printf("%s %s:\nHealth: %d/%d, Def: %s\n", type, getName(), curHealth, maxHealth, defenseR);
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public HashMap<String, Double> getResists() {
            return resists;
        }

        public void setResists(HashMap<String, Double> resists) {
            this.resists = resists;
        }

        public void fillResists(List<Double> resists) {
            this.resists.put("poison", resists.get(0));

        }
    }

    public static class Status extends Obj {
        private int duration;

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getDuration() {
            return duration;
        }

        public void tickDuration() {
            duration--;
        }

        public int tick() {
            return -1;
        }

        public int getVal() {
            return 0;
        }

        public void add(Status status) {
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Status) {
                if (Objects.equals(((Status) obj).getName(), this.getName())) {
                    return true;
                }
            }
            return false;
        }
    }

    public static class Block extends Status {
        private int ddef;

        Block(int duration, int ddef) {
            setDuration(duration);
            this.ddef = ddef;
            this.setName("block");
        }

        public int tick() {
            if (getDuration() >= 1) {
                tickDuration();
                return ddef;
            } else {
                return -1;
            }
        }

        public int getVal() {
            return ddef;
        }

        @Override
        public void add(Status block2) {
            this.ddef += ((Block) block2).ddef;
            this.setDuration(this.getDuration() + 1);
        }
    }

    public static class Poison extends Status {
        private int poison;

        Poison(int poison) {
            this.poison = poison;
            this.setName("poison");
        }

        public int tick() {
            if (poison >= 1) {
                poison--;
                return poison;
            }
            return -1;
        }

        public int getVal() {
            return poison;
        }

        @Override
        public void add(Status poison2) {
            poison += ((Poison) poison2).poison * 2 / 3;
        }
    }

    public static class Stun extends Status {
        Stun(int duration) {
            setDuration(duration);
            this.setName("stun");
        }

        public int tick() {
            if (getDuration() >= 1) {
                tickDuration();
                return 1;
            }
            return -1;
        }
    }

    public static class Hero extends Entity {
        HashMap<String, Double> resists = new HashMap<String, Double>();
        private int actionCnt = 1;

        public void attack(Entity target) {
            target.decreaseHealth(max(this.getAttackR() - target.getDefenseR(), 0));
        }

        public void block() {
            int n = 5;
            this.setStatus(new Block(3 - 1, n));
            setDefenseR(getDefenseR() + n);
        }

        public void triggerStatuses() {
            for (Status s : this.getStatuses()) {
                int t = s.tick();
                if (t == -1) {
                    getStatuses().remove(s);
                    return;
                }
                switch (s.getName()) {
                    case "block":
                        setDefenseR(getDefenseR() + t);
                        break;
                    case "poison":
                        decreaseHealth((int) round(max(t * getResists().get("poison"), 0)));
                        break;
                    case "stun":
                        this.actionCnt = 0;
                        break;
                }
            }
        }

        public void startTurn() {
            setDefenseR(getDefense());
            setAttackR(getAttack());
            this.triggerStatuses();
        }
    }

    public static class Archer extends Hero {
        Archer(String name) {
            List<Double> list = List.of(0.8, 1.);
            this.fillResists(list);
            this.setMaxHealthAndHeal(90);
            this.setAttack(20);
            this.setDefense(0);
            this.setInitiative(5);
            this.setName(name);
            this.setType("Archer");
        }

        public void poisonAttack(Entity target) {
            target.setStatus(new Poison(8 + 1));
        }
    }

    public static class Cleric extends Hero {
        private int healAmount;

        Cleric(String name) {
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

        public void heal(Entity target) {
            target.setMaxHealthAndHeal(target.getMaxHealth() + healAmount);
        }
    }

    public static class Fighter extends Hero {
        Fighter(String name) {
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
            this.setStatus(new Block(3 - 1, n));
            setDefenseR(getDefenseR() + n);
        }

        public void stun(Entity target) {
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

        public void magicMissile(Entity target) {
            target.decreaseHealth(20);//ignores def
        }
    }
    public static class Action{
        private boolean action = true;
        Action() {}
        public void use(){
            action = false;
        }
        public boolean isReady(){
            return action;
        }
        public void refill(){
            action = true;
        }
    }
    public static boolean allDrained(List<Action> actions) {
        boolean allDrained = true;
        for (Action action : actions) {
            if (action.isReady()) {
                allDrained = false;
                return allDrained;
            }
        }
        return allDrained;
    }
    public static void refillAllDrained(List<Action> actions) {
        for (Action action : actions) {
            action.refill();
        }
    }
    public static class Game {
        List<Hero> heroes = new ArrayList<>();
        List<Hero> enemies = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        FileWriter fw = new FileWriter("log.txt");

        Game() throws IOException {
            while (true){
                heroes = new ArrayList<>();
                enemies = new ArrayList<>();
                boolean end = false;
                while (true){
                    System.out.println("Pick hero: (A)rcher, (W)izard, (F)ighter or (C)leric");
                    String input = sc.nextLine();
                    switch (input) {
                        case "end this":
                            end = true;
                            break;
                        case "A":
                            heroes.add(new Archer("1"));
                            break;
                        case "W":
                            heroes.add(new Wizard("1"));
                            break;
                        case "F":
                            heroes.add(new Fighter("1"));
                            break;
                        case "C":
                            heroes.add(new Cleric("1"));
                            break;
                        default:
                            System.out.println("Invalid input");
                            continue;
                    }
                    fw.write(input+'\n');
                    if (end){
                        break;
                    }
                    heroes.get(0).startTurn();
                    break;
                }
                enemies.add(new Fighter("1"));
                for (Hero enemy : enemies) {
                    enemy.startTurn();
                }
                List<Action> actions = new ArrayList<>();
                for (Hero hero : heroes) {
                    actions.add(new Action());
                }

                while (true){
                    System.out.println("choose action, enter help to get help:");
                    String input = sc.nextLine();
                    String[] inputs = input.toLowerCase().split(" ");

                    Hero attacker = null;
                    Hero defender = null;
                    int attInd = 0;
                    if(inputs[0].equals("help")){
                        System.out.println("Typical command consists of 3 words:\n" +
                                "1.Number of your hero starting at 1\n" +
                                "2. Action:\n" +
                                "at - normal attack\n" +
                                "pat - poison attack of archer\n" +
                                "mis - magic missile of wizard\n" +
                                "heal - healing from cleric\n" +
                                "bl - block\n" +
                                "3. Target, also as a number starting at 1\n" +
                                "Note, that block does not need a target\n"+
                                "Also you can print stats by typing stats");
                        continue;
                    } else if (input.equals("end this")){
                        end = true;
                        break;
                    } else if(inputs[0].equals("stats")){
                        for(Hero hero : heroes){
                            hero.printStats();
                        }
                        System.out.println("-------------");
                        for (Hero hero : enemies){
                            hero.printStats();
                        }
                        continue;
                    }else if(inputs[0].equals("1") || inputs[0].equals("2") || inputs[0].equals("3") || inputs[0].equals("4")){
                        if (inputs.length<2){
                            System.out.println("Invalid input");
                            continue;
                        }
                        try {
                            attInd = Integer.parseInt(inputs[0])-1;
                            attacker = heroes.get(attInd);
                            if(!actions.get(attInd).isReady()){
                                attacker = null;
                                System.out.println("Attacker already made an action");
                                continue;
                            }
                            if(attacker.getCurHealth()==0){
                                attacker = null;
                                System.out.println("Attacker died");
                                continue;
                            }
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println("Invalid character chosen");
                            continue;
                        }
                    }
                    if("at pat mis heal".contains(inputs[1])){
                        if(inputs.length<3){
                            System.out.println("No target");
                            continue;
                        }
                        if(Objects.equals(inputs[2], "1") || Objects.equals(inputs[2], "2") || Objects.equals(inputs[2], "3") || Objects.equals(inputs[2], "4")){
                            try {
                                defender = enemies.get(Integer.parseInt(inputs[2])-1);
                            } catch (IndexOutOfBoundsException e) {
                                System.out.println("Invalid target");
                                continue;
                            }
                        }
                        try {
                            switch (inputs[1]) {
                                case "at":
                                    attacker.attack(defender);
                                    break;
                                case "mis":
                                    ((Wizard)attacker).magicMissile(defender);
                                    break;
                                case "pat":
                                    ((Archer)attacker).poisonAttack(defender);
                                    break;
                                case "heal":
                                    ((Cleric)attacker).heal(defender);
                            }
                        } catch (ClassCastException e){
                            System.out.println("Character cant take such action");
                            continue;
                        } catch (NullPointerException e){
                            System.out.println("Invalid choice of letters");
                            continue;
                        }

                    } else if("bl".contains(inputs[1])) {
                        switch (inputs[1]) {
                            case "bl":
                                attacker.block();
                                break;

                        }
                    } else {
                        System.out.println("Invalid action chosen");
                        continue;
                    }
                    actions.get(attInd).use();
                    fw.write(input+"\n");
                    if(allDrained(actions)){
                        refillAllDrained(actions);
                        boolean won = true;
                        for(Hero enemy: enemies){
                            if (enemy.getCurHealth()>0){
                                won = false;
                                enemy.startTurn();
                                enemy.attack(heroes.get(0));
                            }
                        }
                        if(won){
                            System.out.println("You won");
                            break;
                        }
                        for(Hero hero: heroes){
                            hero.startTurn();
                            if(hero.getCurHealth()==0){
                                actions.get(attInd).use();
                            }
                        }
                        if (allDrained(actions)){
                            System.out.println("You died");
                            break;
                        }
                        System.out.println("Next turn");
                    }

                }
                if(end){
                    fw.close();
                    break;
                }
            }
        }

    }

    public static void main(String[] args) throws IOException {
        new Game();
    }
}