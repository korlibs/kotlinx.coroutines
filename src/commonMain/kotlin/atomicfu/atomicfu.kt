package kotlinx.atomicfu

fun <T> atomic(initial: T): AtomicRef<T> = AtomicRef(initial)
fun atomic(initial: Int): AtomicInt = AtomicInt(initial)
fun atomic(initial: Long): AtomicLong = AtomicLong(initial)
fun atomic(initial: Boolean): AtomicBoolean = AtomicBoolean(initial)

open class AtomicBase<T>(initial: T) {
    var value: T = initial
    fun lazySet(value: T) {
        this.value = value
    }

    fun compareAndSet(expect: T, update: T): Boolean {
        if (this.value == expect) {
            this.value = update
            return true
        } else {
            return false
        }
    }

    fun getAndSet(value: T): T {
        val old = this.value
        this.value = value
        return value
    }
}

class AtomicRef<T>(initial: T) : AtomicBase<T>(initial)

class AtomicBoolean(initial: Boolean) : AtomicBase<Boolean>(initial)

class AtomicInt(initial: Int) : AtomicBase<Int>(initial) {
    fun getAndAdd(count: Int) = this.value.also { this.value += count }
    fun addAndGet(count: Int) = getAndAdd(count) + count

    fun incrementAndGet() = addAndGet(+1)
    fun decrementAndGet() = addAndGet(-1)
    fun getAndDecrement() = getAndAdd(-1)
    fun getAndIncrement() = getAndAdd(+1)
}

class AtomicLong(initial: Long) : AtomicBase<Long>(initial) {
    fun getAndAdd(count: Long) = this.value.also { this.value += count }
    fun addAndGet(count: Long) = getAndAdd(count) + count

    fun incrementAndGet() = addAndGet(+1)
    fun decrementAndGet() = addAndGet(-1)
    fun getAndDecrement() = getAndAdd(-1)
    fun getAndIncrement() = getAndAdd(+1)
}

inline fun AtomicInt.updateAndGet(function: (Int) -> Int): Int {
    while (true) {
        val cur = value
        val upd = function(cur)
        if (compareAndSet(cur, upd)) return upd
    }
}

inline fun AtomicLong.updateAndGet(function: (Long) -> Long): Long {
    while (true) {
        val cur = value
        val upd = function(cur)
        if (compareAndSet(cur, upd)) return upd
    }
}

inline fun <T> AtomicRef<T>.loop(action: (T) -> Unit): Nothing {
    while (true) {
        action(value)
    }
}

inline fun AtomicLong.loop(action: (Long) -> Unit): Nothing {
    while (true) {
        action(value)
    }
}

inline fun AtomicInt.loop(action: (Int) -> Unit): Nothing {
    while (true) {
        action(value)
    }
}

inline fun AtomicBoolean.loop(action: (Boolean) -> Unit): Nothing {
    while (true) {
        action(value)
    }
}


inline fun <T> AtomicRef<T>.update(function: (T) -> T) {
    while (true) {
        val cur = value
        val upd = function(cur)
        if (compareAndSet(cur, upd)) return
    }
}

inline fun AtomicInt.update(function: (Int) -> Int) {
    while (true) {
        val cur = value
        val upd = function(cur)
        if (compareAndSet(cur, upd)) return
    }
}

inline fun AtomicLong.update(function: (Long) -> Long) {
    while (true) {
        val cur = value
        val upd = function(cur)
        if (compareAndSet(cur, upd)) return
    }
}

inline fun AtomicBoolean.update(function: (Boolean) -> Boolean) {
    while (true) {
        val cur = value
        val upd = function(cur)
        if (compareAndSet(cur, upd)) return
    }
}
