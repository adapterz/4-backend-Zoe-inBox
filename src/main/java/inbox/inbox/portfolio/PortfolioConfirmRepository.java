package inbox.inbox.portfolio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// DB에 접근하게 해줄 인터페이스
@Repository
public interface PortfolioConfirmRepository extends
    JpaRepository<PortfolioConfirm, Long> {

}
