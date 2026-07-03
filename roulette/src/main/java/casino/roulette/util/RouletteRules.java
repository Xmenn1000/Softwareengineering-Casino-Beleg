package casino.roulette.util;

import casino.roulette.exceptions.BadRouletteRequestException;

import java.math.BigDecimal;
import java.math.MathContext;

public final class RouletteRules {

    private RouletteRules() {
    }

    public static boolean isWinningBet(BetType betType, String betValue, int ballPosition) {
        validateBallPosition(ballPosition);

        if (betType == BetType.STRAIGHT_NUMBER) {
            return isWinningStraightNumberBet(betValue, ballPosition);
        }

        if (betType == BetType.COLOR) {
            return isWinningColorBet(betValue, ballPosition);
        }

        if (betType == BetType.PARITY) {
            return isWinningParityBet(betValue, ballPosition);
        }

        if (betType == BetType.RANGE) {
            return isWinningRangeBet(betValue, ballPosition);
        }

        if (betType == BetType.DOZEN) {
            return isWinningDozenBet(betValue, ballPosition);
        }

        throw new BadRouletteRequestException("Bet type is not supported yet: " + betType);
    }

    private static boolean isWinningStraightNumberBet(String betValue, int ballPosition) {
        int betNumber = parseBetNumber(betValue);

        if (betNumber < 0 || betNumber > 36) {
            throw new BadRouletteRequestException("STRAIGHT_NUMBER bet value must be between 0 and 36");
        }

        return betNumber == ballPosition;
    }

    private static int parseBetNumber(String betValue) {
        String normalizedBetValue = normalize(betValue);

        try {
            return Integer.parseInt(normalizedBetValue);
        } catch (NumberFormatException ex) {
            throw new BadRouletteRequestException("Bet value must be a number");
        }
    }

    private static boolean isWinningColorBet(String betValue, int ballPosition) {
        if (ballPosition == 0) {
            return false;
        }

        String normalizedBetValue = normalize(betValue);

        if (!normalizedBetValue.equals("RED") && !normalizedBetValue.equals("BLACK")) {
            throw new BadRouletteRequestException("COLOR bet value must be RED or BLACK");
        }

        boolean ballIsRed = isRedNumber(ballPosition);

        // BLACK: normalizedBetValue.equals("RED") = false --> ballIsRed = false --> false == false --> true!
        return normalizedBetValue.equals("RED") == ballIsRed;
    }

    private static boolean isWinningParityBet(String betValue, int ballPosition) {
        if (ballPosition == 0) {
            return false;
        }

        String normalizedBetValue = normalize(betValue);

        if (!normalizedBetValue.equals("EVEN") && !normalizedBetValue.equals("ODD")) {
            throw new BadRouletteRequestException("PARITY bet value must be EVEN or ODD");
        }

        boolean ballIsEven = ballPosition % 2 == 0;

        return normalizedBetValue.equals("EVEN") == ballIsEven;
    }

    private static boolean isWinningRangeBet(String betValue, int ballPosition) {
        if (ballPosition == 0) {
            return false;
        }

        String normalizedBetValue = normalize(betValue);

        if (!normalizedBetValue.equals("LOW") && !normalizedBetValue.equals("HIGH")) {
            throw new BadRouletteRequestException("RANGE bet value must be LOW or HIGH");
        }

        boolean ballIsLow = ballPosition >= 1 && ballPosition <= 18;

        return normalizedBetValue.equals("LOW") == ballIsLow;
    }

    private static boolean isWinningDozenBet(String betValue, int ballPosition) {
        if (ballPosition == 0) {
            return false;
        }

        String normalizedBetValue = normalize(betValue);

        if (!normalizedBetValue.equals("FIRST")
                && !normalizedBetValue.equals("SECOND")
                && !normalizedBetValue.equals("THIRD")) {
            throw new BadRouletteRequestException("DOZEN bet value must be FIRST, SECOND or THIRD");
        }

        return switch (normalizedBetValue) {
            case "FIRST" -> ballPosition >= 1 && ballPosition <= 12;
            case "SECOND" -> ballPosition >= 13 && ballPosition <= 24;
            case "THIRD" -> ballPosition >= 25 && ballPosition <= 36;
            default -> false;
        };
    }

    private static void validateBallPosition(int ballPosition) {
        if (ballPosition < 0 || ballPosition > 36) {
            throw new BadRouletteRequestException("Ball position must be between 0 and 36");
        }
    }

    private static String normalize(String value) {
        if (value == null || value.isBlank()) {
            throw new BadRouletteRequestException("Bet value must not be empty");
        }

        return value.trim().toUpperCase();
    }

    private static boolean isRedNumber(int ballPosition) {
        return switch (ballPosition) {
            case 1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36 -> true;
            default -> false;
        };
    }

    public static BigDecimal houseEdge(BetType betType) {
        return BigDecimal.ONE.subtract(returnToPlayer(betType));
    }

    public static BigDecimal returnToPlayer(BetType betType) {
        int totalReturnMultiplier = payoutMultiplier(betType) + 1;

        return hitProbability(betType)
                .multiply(BigDecimal.valueOf(totalReturnMultiplier));
    }

    public static int payoutMultiplier(BetType betType) {
        if (betType == BetType.STRAIGHT_NUMBER) {
            return 35;
        }

        if (betType == BetType.COLOR || betType == BetType.PARITY || betType == BetType.RANGE) {
            return 1;
        }

        if (betType == BetType.DOZEN) {
            return 2;
        }

        throw new BadRouletteRequestException("Bet type is not supported yet: " + betType);
    }

    public static BigDecimal hitProbability(BetType betType) {
        return BigDecimal.valueOf(winningOutcomes(betType))
                .divide(BigDecimal.valueOf(totalOutcomes()), MathContext.DECIMAL64);
    }

    public static int winningOutcomes(BetType betType) {
        return switch (betType) {
            case STRAIGHT_NUMBER -> 1;
            case COLOR, PARITY, RANGE -> 18;
            case DOZEN -> 12;
        };
    }

    public static int totalOutcomes() {
        return 37;
    }
}
