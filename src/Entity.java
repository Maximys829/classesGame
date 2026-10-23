import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Entity extends Obj {

    private String type;
    private int maxHealth = 10;
    private int curHealth = maxHealth;
    private int attack = 0;
    private int attackR = 0;
    private int defense = 0;
    private int defenseR = 0;
    private int initiative = 0;
    private List<Main.Status> statuses = new ArrayList() {
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

    public void setStatus(Main.Status status) {
        if (!statuses.contains(status)) {
            this.statuses.add(status);
        } else {
            statuses.get(statuses.indexOf(status)).add(status);

        }

    }

    public List<Main.Status> getStatuses() {
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