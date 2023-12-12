package com.mis.animatedbutton

/**
 * All possible button animations
 */
enum class ButtonAnimation {
    ResetToNormal,
    NormalToLoading,
    LoadingToNormal,
    LoadingToDone,
    DoneToNormal,
    LoadingToError,
    ErrorToNormal
}