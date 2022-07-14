package inbox.inbox.portfolio;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioFileRepository extends
    JpaRepository<PortfolioFile, Long> {

}
