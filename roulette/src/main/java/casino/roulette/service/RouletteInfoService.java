package casino.roulette.service;

import casino.roulette.util.BetType;
import casino.roulette.util.RouletteRules;
import org.springframework.stereotype.Component;

@Component
public class RouletteInfoService {

    public String getRules() {
        return """
            European roulette uses numbers from 0 to 36. A player places a bet on a supported betting option and the ball lands on one number.
            The supported betting options are:
            STRAIGHT_NUMBER: bet on one number from 0 to 36.
            COLOR: bet on RED or BLACK. Number 0 has no color and loses color bets.
            PARITY: bet on EVEN or ODD. Number 0 is neither even nor odd and loses parity bets.
            RANGE: bet on LOW (1-18) or HIGH (19-36). Number 0 loses range bets.
            DOZEN: bet on FIRST (1-12), SECOND (13-24), or THIRD (25-36). Number 0 loses dozen bets.
            Each API call represents exactly one completed game round.
            """;
    }

    public String getChances() {
        return """
            European roulette has 37 possible ball positions: 0-36.
            STRAIGHT_NUMBER: hit probability 1/37, payout 35:1, RTP 36/37 (%s), house edge 1/37 (%s).
            COLOR: hit probability 18/37, payout 1:1, RTP 36/37 (%s), house edge 1/37 (%s).
            PARITY: hit probability 18/37, payout 1:1, RTP 36/37 (%s), house edge 1/37 (%s).
            RANGE: hit probability 18/37, payout 1:1, RTP 36/37 (%s), house edge 1/37 (%s).
            DOZEN: hit probability 12/37, payout 2:1, RTP 36/37 (%s), house edge 1/37 (%s).
            Profit formula:
            Winning round: amount = betAmount * payoutMultiplier.
            Losing round: amount = -betAmount.
            House edge is caused by number 0.
            """.formatted(
                RouletteRules.returnToPlayer(BetType.STRAIGHT_NUMBER),
                RouletteRules.houseEdge(BetType.STRAIGHT_NUMBER),
                RouletteRules.returnToPlayer(BetType.COLOR),
                RouletteRules.houseEdge(BetType.COLOR),
                RouletteRules.returnToPlayer(BetType.PARITY),
                RouletteRules.houseEdge(BetType.PARITY),
                RouletteRules.returnToPlayer(BetType.RANGE),
                RouletteRules.houseEdge(BetType.RANGE),
                RouletteRules.returnToPlayer(BetType.DOZEN),
                RouletteRules.houseEdge(BetType.DOZEN)
        );
    }
}
