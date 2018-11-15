package example.localcloud.localcloud.models;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import example.localcloud.localcloud.db.Db;

public abstract class APathName {

    private File file;

    protected abstract String getPath();

    protected abstract Db getDb();

    public String getPk() {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        digest.reset();
        byte[] bHash = digest.digest(this.getPath().getBytes());
        return String.format("%0" + (bHash.length * 2) + "X", new BigInteger(1, bHash));

    }

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
