/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.guava.api;

import static com.google.common.collect.ImmutableRangeSet.of;
import static com.google.common.collect.Range.closed;
import static com.google.common.collect.Range.open;
import static com.google.common.collect.TreeRangeSet.create;
import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.internal.ErrorMessages.iterableValuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.iterableValuesToLookForIsNull;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;
import static org.assertj.guava.error.RangeSetShouldEnclose.shouldEnclose;
import static org.assertj.guava.internal.ErrorMessages.rangeSetValuesToLookForIsEmpty;
import static org.assertj.guava.internal.ErrorMessages.rangeSetValuesToLookForIsNull;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableRangeSet;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;

@DisplayName("RangeSetAssert enclosesAll")
class RangeSetAssert_enclosesAll_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    RangeSet<Integer> actual = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).enclosesAll(of(closed(0, 1))));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_range_is_null() {
    // GIVEN
    RangeSet<Integer> actual = create();
    Iterable<Range<Integer>> range = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).enclosesAll(range));
    // THEN
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                         .hasMessage(iterableValuesToLookForIsNull());
  }

  @Test
  void should_fail_if_range_set_is_null() {
    // GIVEN
    RangeSet<Integer> actual = create();
    RangeSet<Integer> rangeSet = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).enclosesAll(rangeSet));
    // THEN
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                         .hasMessage(rangeSetValuesToLookForIsNull());
  }

  @Test
  void should_pass_if_both_expected_range_and_actual_are_empty() {
    // GIVEN
    RangeSet<Integer> actual = create();
    // THEN
    assertThat(actual).enclosesAll(emptySet());
  }

  @Test
  void should_pass_if_both_expected_range__set_and_actual_are_empty() {
    // GIVEN
    RangeSet<Integer> actual = create();
    // THEN
    assertThat(actual).enclosesAll(of());
  }

  @Test
  void should_fail_if_expected_range_is_empty() {
    // GIVEN
    RangeSet<Integer> actual = of(closed(0, 1));
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).enclosesAll(emptySet()));
    // THEN
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                         .hasMessage(iterableValuesToLookForIsEmpty());
  }

  @Test
  void should_fail_if_expected_range_set_is_empty() {
    // GIVEN
    RangeSet<Integer> actual = of(closed(0, 1));
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).enclosesAll(of()));
    // THEN
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                         .hasMessage(rangeSetValuesToLookForIsEmpty());
  }

  @Test
  void should_pass_if_the_given_set_encloses_range() {
    // GIVEN
    RangeSet<Integer> rangeSet = of(closed(0, 100));
    // THEN
    assertThat(rangeSet).enclosesAll(asList(closed(0, 10),
                                            open(50, 60),
                                            open(90, 100)));
  }

  @Test
  void should_pass_if_the_given_set_encloses_range_set() {
    // GIVEN
    RangeSet<Integer> actual = of(closed(0, 100));
    RangeSet<Integer> expected = ImmutableRangeSet.<Integer> builder()
                                                  .add(closed(0, 10))
                                                  .add(open(50, 60))
                                                  .add(open(90, 100))
                                                  .build();
    // THEN
    assertThat(actual).enclosesAll(expected);
  }

  @Test
  void should_fail_if_the_given_set_does_not_enclose_range() {
    // GIVEN
    RangeSet<Integer> actual = of(closed(0, 100));
    List<Range<Integer>> expected = asList(closed(50, 70),
                                           closed(120, 150));
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).enclosesAll(expected));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(shouldEnclose(actual, expected, singletonList(closed(120, 150)))
                                                                                                     .create());
  }

  @Test
  void should_fail_if_the_given_set_does_not_enclose_range_set() {
    // GIVEN
    RangeSet<Integer> actual = of(closed(0, 100));
    RangeSet<Integer> expected = ImmutableRangeSet.<Integer> builder()
                                                  .add(closed(50, 70))
                                                  .add(closed(120, 150))
                                                  .build();

    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).enclosesAll(expected));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(shouldEnclose(actual, expected, singletonList(closed(120, 150)))
                                                                                                     .create());
  }
}
