/**
 *
 */
package one.tracking.framework.tcn.repo;

import org.springframework.data.repository.CrudRepository;
import one.tracking.framework.tcn.entity.Memo;

/**
 * @author Marko Voß
 *
 */
public interface MemoRepository extends CrudRepository<Memo, Long> {

}
