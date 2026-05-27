package casino.banking.util;

import casino.banking.exceptions.GameServiceNotKnownException;

public enum GameService {
    SLOTS,
    ROULETTE;

    public GameService gameServiceFromString(String gameName) {
        for (GameService g : GameService.values()) {
            if (g.name().equalsIgnoreCase(gameName)) {
                return g;
            }
        }
        throw new GameServiceNotKnownException(gameName);
    }

}
