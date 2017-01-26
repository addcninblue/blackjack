package game;

import java.util.ArrayList;

public class Player {
    private String name;
    private ArrayList<Hand> hands;
    private int money;
    private int bet; //the amount bet during a turn
    private boolean insured; //whether or not the player is insured

    public Player(String name){
        this.name = name;
        hands = new ArrayList<>();
        money = 1000;
        bet = 0;
        insured = false;
    }

    public void bet(int betAmt) {
        if (betAmt < 1) {
            throw new IllegalArgumentException("You can't bet below $1!");
        }
        if (betAmt > money) {
            throw new IllegalArgumentException("You don't have enough money to bet!");
        }

        this.money -= betAmt;
        this.bet += betAmt;
    }

    public void resetTurn() {
        hands.clear();
        hands.add(new Hand());
        bet = 0;
        insured = false;
    }

    @Override
    public String toString() {
        return String.format("Player: %s\nMoney: %d", name, money);
    }

    public boolean canSplitHand(Hand hand) {
        return hands.contains(hand) && hands.size() < 4
                && hand.isSplittable()&& getMoney() >= getBet();
    }

    public boolean canDoubleDown() {
        Hand hand = hands.get(0);
        //has not split, has not hit, and has enough money
        return hands.size() == 1 && hand.count() == 2 && !hand.isBlackJack()
                && getMoney() >= getBet();
    }

    public String getName(){
        return name;
    }

    public boolean isInsured() {
        return insured;
    }

    public void setInsured(boolean insured) {
        this.insured = insured;
    }

    public ArrayList<Hand> getHands(){
        return hands;
    }

    public Hand getHand(int i) {
        return hands.get(i);
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getBet() {
        return bet;
    }
}
