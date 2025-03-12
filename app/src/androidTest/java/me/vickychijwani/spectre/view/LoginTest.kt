package me.vickychijwani.spectre.view

import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest

import me.vickychijwani.spectre.R
import me.vickychijwani.spectre.testing.ClearPreferencesRule
import me.vickychijwani.spectre.testing.OkHttpIdlingResourceRule
import me.vickychijwani.spectre.testing.ViewNotVisibleIdlingResource
import me.vickychijwani.spectre.testing.startLogin
import org.junit.After
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/*import android.support.test.espresso.*
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import me.vickychijwani.spectre.R
import me.vickychijwani.spectre.testing.*
import org.junit.*
import org.junit.runner.RunWith*/


// Tests follow Jake Wharton's Robot pattern explained here:
// https://academy.realm.io/posts/kau-jake-wharton-testing-robots/

@RunWith(AndroidJUnit4::class) @LargeTest
class LoginTest {



    @Rule
    @JvmField val mActivityRule = IntentsTestRule(LoginActivity::class.java)
    @Rule @JvmField val mPrefsRule = ClearPreferencesRule()
    @Rule @JvmField val mOkHttpIdlingResourceRule = OkHttpIdlingResourceRule()

    private lateinit var mProgressBarIdlingResource: IdlingResource

    @Before
    fun setup() {
        mProgressBarIdlingResource = ViewNotVisibleIdlingResource(mActivityRule.activity, R.id.progress)
        IdlingRegistry.getInstance().register(mProgressBarIdlingResource)
    }

    @After
    fun teardown() {
        IdlingRegistry.getInstance().unregister(mProgressBarIdlingResource)
    }



    @Test
    fun nonExistentBlog() {
        startLogin {
            blogAddress("nonexistent_blog")
        }.connectToBlog {
            errorMatching("There is no Ghost admin")
        }
    }






}
