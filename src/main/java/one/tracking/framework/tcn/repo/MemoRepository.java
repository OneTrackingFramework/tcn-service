/**
 *
 */
package one.tracking.framework.tcn.repo;

import org.springframework.data.repository.CrudRepository;
import one.tracking.framework.tcn.entity.Memo;

public interface MemoRepository extends CrudRepository<Memo, Long> {

}
