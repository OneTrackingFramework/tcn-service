/**
 *
 */
package one.tracking.framework.tcn.event;

import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * @author Marko Voß
 *
 */
@Data
@Builder
public class TcnEvent {

  private List<String> tcns;
}
