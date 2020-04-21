/**
 *
 */
package one.tracking.framework.tcn.event;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Marko Voß
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TcnEvent {

  private List<String> tcns;
  private String memo;
}
