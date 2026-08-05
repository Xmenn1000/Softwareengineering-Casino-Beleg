package casino.slots.service;

import casino.slots.model.SlotsGameEntity;
import casino.slots.repository.SlotsGameRepository;
import casino.slots.restClient.BankingRestClient;
import casino.slots.view.SlotsStatsDTO;
import casino.slots.view.SlotsStatsUserDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatsServiceImpl implements StatsService {

    private final SlotsGameRepository slotsGameRepository;
    private final SlotsStatsCalculator statsCalculator;
    private final BankingRestClient bankingRestClient;

    public StatsServiceImpl(
            SlotsGameRepository slotsGameRepository,
            SlotsStatsCalculator statsCalculator,
            BankingRestClient bankingRestClient
    ) {
        this.slotsGameRepository = slotsGameRepository;
        this.statsCalculator = statsCalculator;
        this.bankingRestClient = bankingRestClient;
    }

    @Override
    public SlotsStatsDTO getStats() {
        List<SlotsGameEntity> games = slotsGameRepository.findAll();

        return new SlotsStatsDTO(
                statsCalculator.totalClientCount(games),
                statsCalculator.totalGamesCount(games),
                statsCalculator.totalProfit(games),
                statsCalculator.totalCashOut(games),
                statsCalculator.totalTurnover(games)
        );
    }

    @Override
    public SlotsStatsUserDTO getStatsByUserId(Long userId) {
        bankingRestClient.findUserById(userId);

        List<SlotsGameEntity> games = slotsGameRepository.findByUserId(userId);

        return new SlotsStatsUserDTO(
                userId,
                statsCalculator.totalGamesCount(games),
                statsCalculator.totalWinnings(games),
                statsCalculator.totalLosses(games),
                statsCalculator.totalClientProfit(games),
                statsCalculator.totalHouseTurnoverFromClient(games),
                statsCalculator.totalHouseProfitFromClient(games)
        );
    }
}
