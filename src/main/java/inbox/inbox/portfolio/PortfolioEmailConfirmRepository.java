package inbox.inbox.portfolio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioEmailConfirmRepository extends JpaRepository<PortfolioEmailConfirm,Integer> {

}
