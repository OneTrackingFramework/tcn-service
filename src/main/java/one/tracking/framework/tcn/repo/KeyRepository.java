/**
 *
 */
package one.tracking.framework.tcn.repo;

import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import one.tracking.framework.tcn.entity.Key;

/**
 * @author Marko Voß
 *
 */
public interface KeyRepository extends PagingAndSortingRepository<Key, Long> {

  Page<Key> findAllByTimestampCreateAfter(Instant instant, Pageable pageable);
}
