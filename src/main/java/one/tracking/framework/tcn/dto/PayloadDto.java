/**
 *
 */
package one.tracking.framework.tcn.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author Marko Voß
 *
 */
@Data
@Builder
public class PayloadDto {

  private String rvk;
  private String tck;
  private short j1;
  private short j2;
  private String memo;

}
