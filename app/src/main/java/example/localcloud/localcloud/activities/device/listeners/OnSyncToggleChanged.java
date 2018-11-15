package example.localcloud.localcloud.activities.device.listeners;

import android.widget.CompoundButton;

import example.localcloud.localcloud.models.FolderModel;

public class OnSyncToggleChanged implements CompoundButton.OnCheckedChangeListener {

    private FolderModel folderModel;

    public OnSyncToggleChanged(FolderModel folderModel) {
        this.folderModel = folderModel;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.folderModel.setSyncEnabled(isChecked);
    }
}
