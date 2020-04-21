/**
 *
 */
package one.tracking.framework.tcn.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Memo {

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false, length = 256)
  private String value;
}
