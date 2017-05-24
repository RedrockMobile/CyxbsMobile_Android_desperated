package com.mredrock.cyxbs.subscriber;

/**
 * Created by cc on 16/3/20.
 */
public abstract class SubscriberListener<T> {

    public void onStart() {
    }

    public void onCompleted() {
    }

    /**
     * 异常分发机制<br>
     *     如果子类已经处理了这个异常，返回 true ，否则返回 false ，异常将被上层 {@link SimpleSubscriber#onError(Throwable)} 处理<br>
     *     请注意上层基本上只处理简单的网络异常，未知的异常会直接被上层 Toast 出来，请尽可能在子类处理可能遇到的异常<br>
     *     不需要管的异常直接返回 true 就好啦
     * @param e 发生的异常
     * @return 该异常是否被子类处理
     */
    public boolean onError(Throwable e) {
        return false;
    }

    public void onNext(T t) {
    }

}
