package me.vickychijwani.spectre.event;

import retrofit.RetrofitError;

public class FileUploadErrorEvent {

    public final RetrofitError error;

    public FileUploadErrorEvent(RetrofitError error) {
        this.error = error;
    }

}
