package me.vickychijwani.spectre.event;


import androidx.annotation.NonNull;

import me.vickychijwani.spectre.network.ApiFailure;

public class ApiErrorEvent {

    public final ApiFailure apiFailure;

    public ApiErrorEvent(@NonNull ApiFailure apiFailure) {
        this.apiFailure = apiFailure;
    }

}
