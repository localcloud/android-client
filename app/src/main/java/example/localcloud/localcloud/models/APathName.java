package example.localcloud.localcloud.models;

import java.io.File;

public abstract class APathName {

    private File file;

    protected abstract String getPath();

    protected File getFile() {
        if (this.file == null) {
            this.file = new File(this.getPath());
        }
        return this.file;
    }

    public String getName() {
        return this.getFile().getName();
    }

    public String getParent() {
        return this.getFile().getParent();
    }
}
