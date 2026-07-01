package casino.slots.service;

import java.util.Arrays;

public class PlaySlots {
  public static void main(String[] args) {
    long deposit = 67;
    SlotsDomainLogic slots = new SlotsDomainLogic();
    String[] play = slots.play(deposit);

    System.out.print("|");
    for (String s : play) {
      System.out.print(s + "|");
    }

    System.out.println();

    if (slots.hasWon(play)) {
      long wonValue = slots.cashoutCalc(play[0], deposit);
      System.out.println("Deposit: " + deposit + "\n" + "New Value: " + wonValue);
    } else {
      System.out.println("You lost all your money XD \n-" + deposit + "$");
    }
  }
}
