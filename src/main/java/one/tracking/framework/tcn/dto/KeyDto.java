/**
 *
 */
package one.tracking.framework.tcn.dto;

import lombok.Builder;
import lombok.Data;
import one.tracking.framework.tcn.entity.Key;

/**
 * @author Marko Voß
 *
 */
@Data
@Builder
public class KeyDto {

  private String tcn;
  private String memo;

  public static final KeyDto fromEntity(final Key key) {
    return KeyDto.builder()
        .memo(key.getMemo() == null ? null : key.getMemo().getValue())
        .tcn(key.getTcn())
        .build();
  }
}
