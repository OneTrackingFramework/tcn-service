/**
 *
 */
package one.tracking.framework.tcn.dto;

import java.time.Instant;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.Data;

/**
 * @author Marko Voß
 *
 */
@Data
@Builder
public class KeyResultDto implements List<KeyDto> {

  private List<KeyDto> tcns;

  private Instant requestTimestamp;

  /**
   * @param action
   * @see java.lang.Iterable#forEach(java.util.function.Consumer)
   */
  @Override
  public void forEach(final Consumer<? super KeyDto> action) {
    this.tcns.forEach(action);
  }

  /**
   * @return
   * @see java.util.List#size()
   */
  @Override
  public int size() {
    return this.tcns.size();
  }

  /**
   * @return
   * @see java.util.List#isEmpty()
   */
  @Override
  public boolean isEmpty() {
    return this.tcns.isEmpty();
  }

  /**
   * @param o
   * @return
   * @see java.util.List#contains(java.lang.Object)
   */
  @Override
  public boolean contains(final Object o) {
    return this.tcns.contains(o);
  }

  /**
   * @return
   * @see java.util.List#iterator()
   */
  @Override
  public Iterator<KeyDto> iterator() {
    return this.tcns.iterator();
  }

  /**
   * @return
   * @see java.util.List#toArray()
   */
  @Override
  public Object[] toArray() {
    return this.tcns.toArray();
  }

  /**
   * @param <T>
   * @param a
   * @return
   * @see java.util.List#toArray(java.lang.Object[])
   */
  @Override
  public <T> T[] toArray(final T[] a) {
    return this.tcns.toArray(a);
  }

  /**
   * @param e
   * @return
   * @see java.util.List#add(java.lang.Object)
   */
  @Override
  public boolean add(final KeyDto e) {
    return this.tcns.add(e);
  }

  /**
   * @param o
   * @return
   * @see java.util.List#remove(java.lang.Object)
   */
  @Override
  public boolean remove(final Object o) {
    return this.tcns.remove(o);
  }

  /**
   * @param c
   * @return
   * @see java.util.List#containsAll(java.util.Collection)
   */
  @Override
  public boolean containsAll(final Collection<?> c) {
    return this.tcns.containsAll(c);
  }

  /**
   * @param c
   * @return
   * @see java.util.List#addAll(java.util.Collection)
   */
  @Override
  public boolean addAll(final Collection<? extends KeyDto> c) {
    return this.tcns.addAll(c);
  }

  /**
   * @param index
   * @param c
   * @return
   * @see java.util.List#addAll(int, java.util.Collection)
   */
  @Override
  public boolean addAll(final int index, final Collection<? extends KeyDto> c) {
    return this.tcns.addAll(index, c);
  }

  /**
   * @param c
   * @return
   * @see java.util.List#removeAll(java.util.Collection)
   */
  @Override
  public boolean removeAll(final Collection<?> c) {
    return this.tcns.removeAll(c);
  }

  /**
   * @param c
   * @return
   * @see java.util.List#retainAll(java.util.Collection)
   */
  @Override
  public boolean retainAll(final Collection<?> c) {
    return this.tcns.retainAll(c);
  }

  /**
   * @param operator
   * @see java.util.List#replaceAll(java.util.function.UnaryOperator)
   */
  @Override
  public void replaceAll(final UnaryOperator<KeyDto> operator) {
    this.tcns.replaceAll(operator);
  }

  /**
   * @param filter
   * @return
   * @see java.util.Collection#removeIf(java.util.function.Predicate)
   */
  @Override
  public boolean removeIf(final Predicate<? super KeyDto> filter) {
    return this.tcns.removeIf(filter);
  }

  /**
   * @param c
   * @see java.util.List#sort(java.util.Comparator)
   */
  @Override
  public void sort(final Comparator<? super KeyDto> c) {
    this.tcns.sort(c);
  }

  /**
   *
   * @see java.util.List#clear()
   */
  @Override
  public void clear() {
    this.tcns.clear();
  }

  /**
   * @param index
   * @return
   * @see java.util.List#get(int)
   */
  @Override
  public KeyDto get(final int index) {
    return this.tcns.get(index);
  }

  /**
   * @param index
   * @param element
   * @return
   * @see java.util.List#set(int, java.lang.Object)
   */
  @Override
  public KeyDto set(final int index, final KeyDto element) {
    return this.tcns.set(index, element);
  }

  /**
   * @param index
   * @param element
   * @see java.util.List#add(int, java.lang.Object)
   */
  @Override
  public void add(final int index, final KeyDto element) {
    this.tcns.add(index, element);
  }

  /**
   * @return
   * @see java.util.Collection#stream()
   */
  @Override
  public Stream<KeyDto> stream() {
    return this.tcns.stream();
  }

  /**
   * @param index
   * @return
   * @see java.util.List#remove(int)
   */
  @Override
  public KeyDto remove(final int index) {
    return this.tcns.remove(index);
  }

  /**
   * @return
   * @see java.util.Collection#parallelStream()
   */
  @Override
  public Stream<KeyDto> parallelStream() {
    return this.tcns.parallelStream();
  }

  /**
   * @param o
   * @return
   * @see java.util.List#indexOf(java.lang.Object)
   */
  @Override
  public int indexOf(final Object o) {
    return this.tcns.indexOf(o);
  }

  /**
   * @param o
   * @return
   * @see java.util.List#lastIndexOf(java.lang.Object)
   */
  @Override
  public int lastIndexOf(final Object o) {
    return this.tcns.lastIndexOf(o);
  }

  /**
   * @return
   * @see java.util.List#listIterator()
   */
  @Override
  public ListIterator<KeyDto> listIterator() {
    return this.tcns.listIterator();
  }

  /**
   * @param index
   * @return
   * @see java.util.List#listIterator(int)
   */
  @Override
  public ListIterator<KeyDto> listIterator(final int index) {
    return this.tcns.listIterator(index);
  }

  /**
   * @param fromIndex
   * @param toIndex
   * @return
   * @see java.util.List#subList(int, int)
   */
  @Override
  public List<KeyDto> subList(final int fromIndex, final int toIndex) {
    return this.tcns.subList(fromIndex, toIndex);
  }

  /**
   * @return
   * @see java.util.List#spliterator()
   */
  @Override
  public Spliterator<KeyDto> spliterator() {
    return this.tcns.spliterator();
  }


}
