package casino.slots.service;

import org.springframework.stereotype.Service;

@Service
public class InfoServiceImpl implements InfoService {
    @Override
    public String getRules() {
        StringBuilder sBuilder = new StringBuilder();
        return """
            Slots - Game Rules
            How can I lose my money playing Slots? Good question. Here are the rules.

            The goal:
            Pull the lever, spin 3 reels, and pray to the SEVENs. That's it. That's the strategy.

            How the magic happens:
            - Pick a bet amount. This is the money you are about to say goodbye to.
            - Hit spin. Each of the 3 reels lands on a random symbol.
            - From "meh" to "call your family": CHERRY, LEMON, ORANGE, PLUM, GOLDBAR, SEVEN.

            When you actually win (it happens, occasionally):
            - Only your best matching combo counts. We are generous like that. Once. I Mean the House always wins right? right!
            - Three of a kind is the jackpot dream. Chase it responsibly. Or don't.
            - GOLDBAR and SEVEN are so fancy that even two of them pay out, But dont count on them. 
              and a lonely single SEVEN still tips you a little.
            - Want the cold, hard numbers? They live over at /chances.

            The fine print (aka how the money leaves):
            - Every spin quietly subtracts your bet from your balance.
            - Win, and you get your bet back times a multiplier. Cha-ching.
            - Lose, and... well. The bet is gone. 
            Always Rember the House Always wins. :) 
            """;
    }

    @Override
    public String getChances() {
        return "";
    }
}
