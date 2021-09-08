package com.ulegalize.lawfirm.utils;

import java.util.List;
import java.util.function.BiFunction;

import static java.util.stream.Collectors.toList;

public interface SuperTriConverter<A, B, C> extends BiFunction<A, B, C> {
  default List<C> convertToList(final List<A> input, B bool) {
    return input.stream().map((A t) -> apply(t, bool)).collect(toList());
  }
}