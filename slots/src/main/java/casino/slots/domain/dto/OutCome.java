package casino.slots.domain.dto;

import casino.slots.domain.enums.Symbol;

import java.util.List;

public class OutCome {

    private final List<Symbol> symbols;

    public OutCome(List<Symbol> symbols) {
        this.symbols = symbols;
    }

    public List<Symbol> getOutCome() {
        return this.symbols;
    }


    public int numberOfSymbols(Symbol symbol) {
        return Math.toIntExact(symbols.stream().filter((singleSymbol) -> singleSymbol == symbol).count());
    }

    public boolean isAllSameSymbol(Symbol symbol) {
        return symbols.stream().filter((singleSymbol) -> singleSymbol == symbol).count() == symbols.size();
    }
}
