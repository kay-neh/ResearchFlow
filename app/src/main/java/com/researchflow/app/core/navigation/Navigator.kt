package com.researchflow.app.core.navigation

import androidx.navigation3.runtime.NavKey

class Navigator(
    val state: NavigationState
) {

    fun navigate(route: NavKey) {
        println("NAVIGATE → $route")
        println("Current topLevelRoute BEFORE = ${state.topLevelRoute}")

        if (route in state.backStacks.keys) {
            state.topLevelRoute = route
        } else {
            state.backStacks[state.topLevelRoute]?.add(route)
        }

        println("Current topLevelRoute AFTER = ${state.topLevelRoute}")
        println("Stacks = ${state.backStacks}")
    }

    fun goBack() {
        val currentStack = state.backStacks[state.topLevelRoute]
            ?: error("Stack for ${state.topLevelRoute} not found")

        val currentRoute = currentStack.last()

        if (currentRoute == state.topLevelRoute) {
            state.topLevelRoute = state.startRoute
        } else {
            currentStack.removeLastOrNull()
        }
    }

    fun logout() {
        state.topLevelRoute = state.startRoute
    }
}