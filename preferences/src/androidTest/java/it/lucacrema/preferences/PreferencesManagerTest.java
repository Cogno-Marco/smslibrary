package it.lucacrema.preferences;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class PreferencesManagerTest {

    private static final String DEFAULT_INT_KEY = "defaultIntKey";
    private static final String DEFAULT_STRING_KEY = "defaultStringKey";
    private static final String DEFAULT_BOOL_KEY = "defaultBooleanKey";
    private static final String DEFAULT_OBJECT_KEY = "defaultObjectKey";
    private static final int DEFAULT_INT_VALUE = 100;
    private static final String DEFAULT_STRING_VALUE = "Roberto";
    private static final boolean DEFAULT_BOOL_VALUE = true;
    private static final String DEFAULT_OBJECT_VALUE = "Test String";
    private static final int MAX_SHIFT_VALUE = 3;
    private Context ctx;

    @Before
    public void setUp() {
        ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
        PreferencesManager.removeAllValues(ctx);
    }

    @Test
    public void getInt_defaultInteger_isEquals() {
        Assert.assertEquals(PreferencesManager.DEFAULT_INTEGER_RETURN, PreferencesManager.getInt(ctx, DEFAULT_INT_KEY));
    }

    @Test
    public void getString_defaultString_isEquals() {
        Assert.assertEquals(PreferencesManager.DEFAULT_STRING_RETURN, PreferencesManager.getString(ctx, DEFAULT_STRING_KEY));
    }

    @Test
    public void getObject_isNull() {
        Assert.assertNull(PreferencesManager.getObject(ctx, DEFAULT_OBJECT_KEY));
    }

    @Test
    public void getBoolean_defaultBoolean_isEquals() {
        Assert.assertEquals(PreferencesManager.DEFAULT_BOOLEAN_RETURN, PreferencesManager.getBoolean(ctx, DEFAULT_BOOL_KEY));
    }

    @Test
    public void setInt_getInt_isEquals() {
        PreferencesManager.setInt(ctx, DEFAULT_INT_KEY, DEFAULT_INT_VALUE);
        Assert.assertEquals(DEFAULT_INT_VALUE, PreferencesManager.getInt(ctx, DEFAULT_INT_KEY));
    }

    @Test
    public void setString_getString_isEquals() {
        PreferencesManager.setString(ctx, DEFAULT_STRING_KEY, DEFAULT_STRING_VALUE);
        Assert.assertEquals(DEFAULT_STRING_VALUE, PreferencesManager.getString(ctx, DEFAULT_STRING_KEY));
    }

    @Test
    public void setBoolean_getBoolean_isEquals() {
        PreferencesManager.setBoolean(ctx, DEFAULT_BOOL_KEY, DEFAULT_BOOL_VALUE);
        Assert.assertEquals(DEFAULT_BOOL_VALUE, PreferencesManager.getBoolean(ctx, DEFAULT_BOOL_KEY));
    }

    @Test
    public void setObject_getBObject_isEquals() {
        try {
            PreferencesManager.setObject(ctx, DEFAULT_OBJECT_KEY, DEFAULT_OBJECT_VALUE);
        } catch (IOException e) {
            Assert.fail("Should not have thrown an IOException");
        }
        Assert.assertEquals(DEFAULT_OBJECT_VALUE, PreferencesManager.<String>getObject(ctx, DEFAULT_OBJECT_KEY));
    }

    @Test
    public void updateInt_getInt_isEquals() {
        PreferencesManager.updateInt(ctx, DEFAULT_INT_KEY);
        Assert.assertEquals(PreferencesManager.DEFAULT_UPDATE_INT_ADD, PreferencesManager.getInt(ctx, DEFAULT_INT_KEY));
    }

    @Test
    public void updateInt_twice_getInt_isEquals() {
        PreferencesManager.updateInt(ctx, DEFAULT_INT_KEY, DEFAULT_INT_VALUE);
        PreferencesManager.updateInt(ctx, DEFAULT_INT_KEY, DEFAULT_INT_VALUE);
        Assert.assertEquals(2 * DEFAULT_INT_VALUE, PreferencesManager.getInt(ctx, DEFAULT_INT_KEY));
    }

    @Test
    public void shiftInt_oneStep_getInt_isEquals() {
        PreferencesManager.shiftInt(ctx, DEFAULT_INT_KEY, MAX_SHIFT_VALUE);
        Assert.assertEquals(1, PreferencesManager.getInt(ctx, DEFAULT_INT_KEY));
    }

    /**
     * We shift the integer so much it goes back to 1
     */
    @Test
    public void shiftInt_maxSteps_getInt_isRestarted() {
        for (int i = 0; i < MAX_SHIFT_VALUE + 1; i++) {
            PreferencesManager.shiftInt(ctx, DEFAULT_INT_KEY, MAX_SHIFT_VALUE);
        }
        Assert.assertEquals(1, PreferencesManager.getInt(ctx, DEFAULT_INT_KEY));
    }

    @Test
    public void shiftInt_moreSteps_getInt_isFull() {
        for (int i = 0; i < MAX_SHIFT_VALUE; i++) {
            PreferencesManager.shiftInt(ctx, DEFAULT_INT_KEY, MAX_SHIFT_VALUE);
        }
        Assert.assertEquals(MAX_SHIFT_VALUE, PreferencesManager.getInt(ctx, DEFAULT_INT_KEY));
    }

    @Test
    public void removeValue_getInt_isEmpty() {
        PreferencesManager.setInt(ctx, DEFAULT_INT_KEY, DEFAULT_INT_VALUE);
        PreferencesManager.removeValue(ctx, DEFAULT_INT_KEY);
        Assert.assertEquals(PreferencesManager.DEFAULT_INTEGER_RETURN, PreferencesManager.getInt(ctx, DEFAULT_INT_KEY));
    }

    @Test
    public void removeValue_getString_isEmpty() {
        PreferencesManager.setString(ctx, DEFAULT_STRING_KEY, DEFAULT_STRING_VALUE);
        PreferencesManager.removeValue(ctx, DEFAULT_STRING_KEY);
        Assert.assertEquals(PreferencesManager.DEFAULT_STRING_RETURN, PreferencesManager.getString(ctx, DEFAULT_STRING_KEY));
    }

    @Test
    public void removeValue_getBoolean_isEmpty() {
        PreferencesManager.setBoolean(ctx, DEFAULT_BOOL_KEY, DEFAULT_BOOL_VALUE);
        PreferencesManager.removeValue(ctx, DEFAULT_BOOL_KEY);
        Assert.assertEquals(PreferencesManager.DEFAULT_BOOLEAN_RETURN, PreferencesManager.getBoolean(ctx, DEFAULT_BOOL_KEY));
    }

    @Test
    public void removeAllValues_getInt_isEmpty() {
        PreferencesManager.setInt(ctx, DEFAULT_INT_KEY, DEFAULT_INT_VALUE);
        PreferencesManager.removeAllValues(ctx);
        Assert.assertEquals(PreferencesManager.DEFAULT_INTEGER_RETURN, PreferencesManager.getInt(ctx, DEFAULT_INT_KEY));
    }

    @Test
    public void removeAllValues_getString_isEmpty() {
        PreferencesManager.setString(ctx, DEFAULT_STRING_KEY, DEFAULT_STRING_VALUE);
        PreferencesManager.removeAllValues(ctx);
        Assert.assertEquals(PreferencesManager.DEFAULT_STRING_RETURN, PreferencesManager.getString(ctx, DEFAULT_STRING_KEY));
    }

    @Test
    public void removeAllValues_getIBoolean_isEmpty() {
        PreferencesManager.setBoolean(ctx, DEFAULT_BOOL_KEY, DEFAULT_BOOL_VALUE);
        PreferencesManager.removeAllValues(ctx);
        Assert.assertEquals(PreferencesManager.DEFAULT_BOOLEAN_RETURN, PreferencesManager.getBoolean(ctx, DEFAULT_BOOL_KEY));
    }
}