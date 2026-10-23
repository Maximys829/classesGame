import java.util.List;

public class Fighter extends Hero {
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
        this.setStatus(new Main.Block(3 - 1, n));
        setDefenseR(getDefenseR() + n);
    }

    public void stun(Main.Entity target) {
        target.setStatus(new Main.Stun(1));
    }
}
