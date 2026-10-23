import java.util.List;

public class Cleric extends Hero {
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
        target.increaseHealth(healAmount);
    }
}