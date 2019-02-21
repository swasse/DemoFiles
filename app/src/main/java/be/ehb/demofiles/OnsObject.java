package be.ehb.demofiles;

import java.io.Serializable;

/**
 * Created by ontlener on 20/01/2038. ;)
 */
public class OnsObject implements Serializable {

    private String onzeString;

    public OnsObject() {
    }

    public OnsObject(String onzeString) {
        this.onzeString = onzeString;
    }

    public String getOnzeString() {
        return onzeString;
    }

    public void setOnzeString(String onzeString) {
        this.onzeString = onzeString;
    }
}
