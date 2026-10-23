import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static java.lang.Math.*;


public class Main {
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

    public static class Action {
        private boolean action = true;

        Action() {
        }

        public void use() {
            action = false;
        }

        public boolean isReady() {
            return action;
        }

        public void refill() {
            action = true;
        }
    }

    public static boolean allDrained(List<Hero> heros) {
        boolean allDrained = true;
        for (Hero hero : heros) {
            if (hero.isReady()) {
                allDrained = false;
                return allDrained;
            }
        }
        return allDrained;
    }

    public static class Game {
        List<Hero> heroes = new ArrayList<>();
        List<Hero> enemies = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        FileWriter fw = new FileWriter("log.txt");
        final String endL = "end this";

        public Game() throws IOException {

        }

        public void playGame() throws IOException {
            boolean end = false;
            while (true) {
                heroes = new ArrayList<>();
                enemies = new ArrayList<>();
                if (end) {
                    fw.close();
                    break;
                }
                while (true) {
                    System.out.println("Pick hero: (A)rcher, (W)izard, (F)ighter or (C)leric");
                    String input = sc.nextLine();
                    switch (input) {
                        case endL:
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
                    fw.write(input + '\n');
                    if (end) {
                        fw.close();
                        break;
                    }
                    heroes.get(0).startTurn();
                    break;
                }

                if (end) {
                    fw.close();
                    break;
                }
                enemies.add(new Fighter("1"));
                for (Hero enemy : enemies) {
                    enemy.startTurn();
                }

                while (true) {
                    System.out.println("write a command, enter help to get help:");
                    String input = sc.nextLine();
                    String[] inputs = input.toLowerCase().split(" ");

                    Hero attacker = null;
                    Hero defender = null;
                    int attInd = 0;
                    if (inputs[0].equals("help")) {
                        System.out.println("Typical command consists of 3 words:\n" +
                                "1.Number of your hero starting at 1\n" +
                                "2. Action:\n" +
                                "at - normal attack\n" +
                                "pat - poison attack of archer\n" +
                                "mis - magic missile of wizard\n" +
                                "heal - healing from cleric\n" +
                                "bl - block\n" +
                                "3. Target, also as a number starting at 1\n" +
                                "Note, that block does not need a target\n" +
                                "Also you can print stats by typing stats\n");
                        continue;
                    } else if (input.equals(endL)) {
                        end = true;
                        break;
                    } else if (inputs[0].equals("stats")) {
                        for (Hero hero : heroes) {
                            hero.printStats();
                        }
                        System.out.println("-------------");
                        for (Hero hero : enemies) {
                            hero.printStats();
                        }
                        continue;
                    } else if (inputs[0].equals("1") || inputs[0].equals("2") || inputs[0].equals("3") || inputs[0].equals("4")) {
                        if (inputs.length < 2) {
                            System.out.println("Invalid input");
                            continue;
                        }
                        try {
                            attInd = Integer.parseInt(inputs[0]) - 1;
                            attacker = heroes.get(attInd);
                            if (!heroes.get(attInd).isReady()) {
                                attacker = null;
                                System.out.println("Attacker already made an action");
                                continue;
                            }
                            if (attacker.getCurHealth() == 0) {
                                attacker = null;
                                System.out.println("Attacker died");
                                continue;
                            }
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println("Invalid character chosen");
                            continue;
                        }
                        if ("at pat mis heal".contains(inputs[1])) {
                            if (inputs.length < 3) {
                                System.out.println("No target");
                                continue;
                            }
                            if (Objects.equals(inputs[2], "1") || Objects.equals(inputs[2], "2") || Objects.equals(inputs[2], "3") || Objects.equals(inputs[2], "4")) {
                                try {
                                    defender = enemies.get(Integer.parseInt(inputs[2]) - 1);
                                } catch (IndexOutOfBoundsException e) {
                                    System.out.println("Invalid target");
                                    continue;
                                }
                            } else {
                                System.out.println("Wrong target");
                                continue;
                            }
                            try {
                                switch (inputs[1]) {
                                    case "at":
                                        attacker.attack(defender);
                                        break;
                                    case "mis":
                                        ((Wizard) attacker).magicMissile(defender);
                                        break;
                                    case "pat":
                                        ((Archer) attacker).poisonAttack(defender);
                                        break;
                                    case "heal":
                                        ((Cleric) attacker).heal(defender);
                                        break;
                                }
                            } catch (ClassCastException e) {
                                System.out.println("Character cant take such action");
                                continue;
                            } catch (NullPointerException e) {
                                System.out.println("Invalid choice of letters");
                                continue;
                            }
                        } else if ("bl".contains(inputs[1])) {
                            switch (inputs[1]) {
                                case "bl":
                                    attacker.block();
                                    break;

                            }
                        } else {
                            System.out.println("Invalid action chosen");
                            continue;
                        }
                        heroes.get(attInd).takeAction();
                        fw.write(input + "\n");
                        if (allDrained(heroes)) {
                            boolean won = true;
                            for (Hero enemy : enemies) {
                                if (enemy.getCurHealth() > 0) {
                                    won = false;
                                    enemy.startTurn();
                                    enemy.attack(heroes.get(0));
                                }
                            }
                            if (won) {
                                System.out.println("You won");
                                break;
                            }
                            for (Hero hero : heroes) {
                                hero.startTurn();
                                if (hero.getCurHealth() == 0) {
                                    heroes.get(attInd).takeAction();
                                }
                            }
                            if (allDrained(heroes)) {
                                System.out.println("You died");
                                break;
                            }
                            System.out.println("Next turn");
                        }

                    } else {
                        System.out.println("Invalid command: " + inputs[0] + " not recognised");
                    }
                    if (end) {
                        fw.close();
                        break;
                    }
                }
            }
        }

    }

    public static void main(String[] args) throws IOException {
        Game game = new Game();
        game.playGame();
    }
}