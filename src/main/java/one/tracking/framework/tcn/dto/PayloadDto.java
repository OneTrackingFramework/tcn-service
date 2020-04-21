/**
 *
 */
package one.tracking.framework.tcn.dto;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

/**
 * @author Marko Voß
 *
 */
@Data
@Builder
public class PayloadDto {

  @NotBlank
  private String rvk;

  @NotBlank
  private String tck;

  private int j1;

  private int j2;

  private String memo;

}
