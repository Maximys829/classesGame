import java.util.List;

public class Wizard extends Hero {
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