package com.migren.composemultiplatform

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform