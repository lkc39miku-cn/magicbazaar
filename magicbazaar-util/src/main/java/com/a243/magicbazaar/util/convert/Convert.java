package com.a243.magicbazaar.util.convert;

import java.util.List;

public interface Convert<S, T> {
    T convert(S s);

    List<T> convert(List<S> sList);
}
