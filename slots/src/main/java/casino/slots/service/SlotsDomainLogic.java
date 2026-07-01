package casino.slots.service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SlotsDomainLogic {

  public String[] play(long deposit) {

    Random random = new Random();

    List<String> slotIcons = List.of(
      "Cherry",
      "Lemon",
      "Cherry",
      "Orange",
      "Watermelon",
      "Lemon",
      "Cherry",
      "Plum",
      "BAR",
      "Cherry",
      "Orange",
      "7",
      "Lemon",
      "Watermelon",
      "Plum",
      "Orange",
      "Bell",
      "Cherry",
      "Lemon",
      "Cherry");

    String[] resultSlot = new String[3];

    for (int i = 0; i < 3; i++) {
      int randomNumber = random.nextInt(slotIcons.size());
      resultSlot[i] = slotIcons.get(randomNumber);
    }

    return resultSlot;
  }

  public boolean hasWon(String[] result) {
    String firstSlot = result[0];
    int counter = 1;
    for (int i = 1; i < 3 ; i++) {
      if (result[i].equals(firstSlot)) {
        counter++;
      }
    }

    return counter == 3 ? true : false;
  }

  public long cashoutCalc(String slotIcon, long deposit) {
    switch (slotIcon) {
      case "Cherry":
        return deposit*2;
      case "Lemon":
        return deposit*3;
      case "Orange":
        return deposit*5;
      case "Plum":
        return deposit*8;
      case "Watermelon":
        return deposit*10;
      case "Bell":
        return deposit*20;
      case "Bar":
        return deposit*40;
      case "7":
        return deposit*100;
    }
    return deposit;
  }

}
