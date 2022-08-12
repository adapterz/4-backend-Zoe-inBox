package inbox.inbox.portfolio;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PortfolioRepository extends
    JpaRepository<Portfolio, Long> {

    @Query(value = "select * from portfolio where not portfolio.portfolio_idx = ?1", nativeQuery = true)
    List<Portfolio> findAllNotPreviousSeenIdx(long previousSeenIdx);
    @Query(value = "select * from portfolio where portfolio.range_val = ?1 and not portfolio.portfolio_idx = ?2", nativeQuery = true)
    List<Portfolio> findByRangeAllNotPreviousSeenIdx(byte range, long previousSeenIdx);
}
