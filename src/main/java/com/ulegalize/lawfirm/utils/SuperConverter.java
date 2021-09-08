package com.ulegalize.lawfirm.utils;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public interface SuperConverter<A, B> extends Function<A, B> {
    default List<B> convertToList(final List<A> input) {
        return input.stream().map(this::apply).collect(toList());
    }

}
