import java.util.List;

public class Archer extends Hero {
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
        target.setStatus(new Main.Poison(8 + 1));
    }
}