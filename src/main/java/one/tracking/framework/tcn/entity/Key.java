/**
 *
 */
package one.tracking.framework.tcn.entity;

import java.time.Instant;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {
    @Index(name = "IDX_TIMESTAMP_CREATE", columnList = "timestampCreate"),
})
public class Key {

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private String tcn;

  @Column(nullable = false, updatable = false)
  private Instant timestampCreate;

  @ManyToOne(cascade = CascadeType.ALL)
  private Memo memo;

  @PrePersist
  void onPrePersist() {
    if (this.id == null) {
      setTimestampCreate(Instant.now());
    }
  }
}
