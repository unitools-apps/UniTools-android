package com.github.ali77gh.unitools.core.onlineapi;

/**
 * Created by ali77gh on 12/11/18.
 */

public interface Promise<T> {

    void onFailed(String msg);
    void onSuccess(T output);
}
