package com.monnl.habitual.utils

inline fun <T> T.modifyIf(condition: Boolean, block: T.() -> T): T = let {
    if (condition) block(this) else this
}