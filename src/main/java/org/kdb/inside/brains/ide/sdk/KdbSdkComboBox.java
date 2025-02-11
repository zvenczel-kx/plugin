package org.kdb.inside.brains.ide.sdk;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.ui.ProjectJdksEditor;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.ComboboxWithBrowseButton;
import com.intellij.ui.SimpleTextAttributes;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unchecked")
public class KdbSdkComboBox extends ComboboxWithBrowseButton {
    public KdbSdkComboBox(boolean projectLevel) {
        getComboBox().setRenderer(new ColoredListCellRenderer<>() {
            @Override
            protected void customizeCellRenderer(@NotNull JList list, Object value, int index, boolean selected, boolean hasFocus) {
                if (value instanceof Sdk) {
                    append(((Sdk) value).getName());
                } else {
                    append(projectLevel ? "No SDK is required" : "Inherit project SDK", SimpleTextAttributes.GRAY_ATTRIBUTES);
                }
            }
        });
        addActionListener(e -> {
            Sdk selectedSdk = getSelectedSdk();
            final Project project = ProjectManager.getInstance().getDefaultProject();
            ProjectJdksEditor editor = new ProjectJdksEditor(selectedSdk, project, KdbSdkComboBox.this);
            editor.show();
            if (editor.isOK()) {
                selectedSdk = editor.getSelectedJdk();
                updateSdkList(selectedSdk);
            }
        });
        updateSdkList(null);
    }

    public void updateSdkList(Sdk sdkToSelect) {
        final List<Sdk> sdkList = ProjectJdkTable.getInstance().getSdksOfType(KdbSdkType.getInstance());
        sdkList.add(0, null);
        getComboBox().setModel(new DefaultComboBoxModel<>(sdkList.toArray(Sdk[]::new)));
        getComboBox().setSelectedItem(sdkToSelect);
    }

    public Sdk getSelectedSdk() {
        return (Sdk) getComboBox().getSelectedItem();
    }

    public void setSelectedSdk(Sdk sdk) {
        getComboBox().setSelectedItem(findSdk(sdk));
    }

    private Sdk findSdk(Sdk sdk) {
        if (sdk == null) {
            return null;
        }

        final ComboBoxModel<Sdk> model = getComboBox().getModel();
        for (int i = 0; i < model.getSize(); i++) {
            final Sdk msdk = model.getElementAt(i);
            if (msdk != null && Objects.equals(sdk.getName(), msdk.getName())) {
                return msdk;
            }
        }
        return null;
    }
}
