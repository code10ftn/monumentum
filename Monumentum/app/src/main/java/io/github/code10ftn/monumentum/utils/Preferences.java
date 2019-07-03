package io.github.code10ftn.monumentum.utils;

import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref(SharedPref.Scope.UNIQUE)
public interface Preferences {

    String token();

    @DefaultBoolean(true)
    boolean keepLogin();

    @DefaultString(Constants.KILOMETERS)
    String units();
}
