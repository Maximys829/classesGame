import java.util.HashMap;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.round;

public class Hero extends Entity {
    private Main.Action action = new Main.Action();
    HashMap<String, Double> resists = new HashMap<String, Double>();

    public void takeAction() {
        action.use();
    }

    public boolean isReady() {
        return action.isReady();
    }

    public void attack(Entity target) {
        target.decreaseHealth(max(this.getAttackR() - target.getDefenseR(), 0));
    }

    public void block() {
        int n = 5;
        this.setStatus(new Main.Block(3 - 1, n));
        setDefenseR(getDefenseR() + n);
    }

    public void triggerStatuses() {
        for (Main.Status s : this.getStatuses()) {
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
                    action.use();
                    break;
            }
        }
    }

    public void startTurn() {
        action.refill();
        setDefenseR(getDefense());
        setAttackR(getAttack());
        this.triggerStatuses();

    }
}