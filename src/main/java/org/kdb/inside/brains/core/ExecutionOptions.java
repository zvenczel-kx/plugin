package org.kdb.inside.brains.core;

import org.kdb.inside.brains.lang.binding.EditorsBindingStrategy;
import org.kdb.inside.brains.settings.SettingsBean;

import java.util.Objects;

public class ExecutionOptions implements SettingsBean<ExecutionOptions> {
    private boolean normalizeQuery = true;
    private boolean showConnectionChange = true;
    private int connectionChangeTimeout = 1000;
    private int warningMessageMb = 10;
    private boolean logQueries = true;

    private EditorsBindingStrategy bindingStrategy = EditorsBindingStrategy.CONNECT_TO_TAB;

    public EditorsBindingStrategy getBindingStrategy() {
        return bindingStrategy;
    }

    public void setBindingStrategy(EditorsBindingStrategy bindingStrategy) {
        this.bindingStrategy = bindingStrategy;
    }

    public boolean isShowConnectionChange() {
        return showConnectionChange;
    }

    public void setShowConnectionChange(boolean showConnectionChange) {
        this.showConnectionChange = showConnectionChange;
    }

    public int getConnectionChangeTimeout() {
        return connectionChangeTimeout;
    }

    public void setConnectionChangeTimeout(int connectionChangeTimeout) {
        this.connectionChangeTimeout = connectionChangeTimeout;
    }

    public boolean isNormalizeQuery() {
        return normalizeQuery;
    }

    public void setNormalizeQuery(boolean normalizeQuery) {
        this.normalizeQuery = normalizeQuery;
    }

    public int getWarningMessageMb() {
        return warningMessageMb;
    }

    public void setWarningMessageMb(int warningMessageMb) {
        this.warningMessageMb = warningMessageMb;
    }

    public boolean isLogQueries() {
        return logQueries;
    }

    public void setLogQueries(boolean logQueries) {
        this.logQueries = logQueries;
    }

    @Override
    public void copyFrom(ExecutionOptions executionOptions) {
        bindingStrategy = executionOptions.bindingStrategy;
        normalizeQuery = executionOptions.normalizeQuery;
        showConnectionChange = executionOptions.showConnectionChange;
        connectionChangeTimeout = executionOptions.connectionChangeTimeout;
        warningMessageMb = executionOptions.warningMessageMb;
        logQueries = executionOptions.logQueries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExecutionOptions that = (ExecutionOptions) o;
        return normalizeQuery == that.normalizeQuery && showConnectionChange == that.showConnectionChange && connectionChangeTimeout == that.connectionChangeTimeout && warningMessageMb == that.warningMessageMb && logQueries == that.logQueries && bindingStrategy == that.bindingStrategy;
    }

    @Override
    public int hashCode() {
        return Objects.hash(normalizeQuery, showConnectionChange, connectionChangeTimeout, warningMessageMb, logQueries, bindingStrategy);
    }

    @Override
    public String toString() {
        return "ExecutionOptions{" +
                "normalizeQuery=" + normalizeQuery +
                ", showConnectionChange=" + showConnectionChange +
                ", connectionChangeTimeout=" + connectionChangeTimeout +
                ", warningMessageMb=" + warningMessageMb +
                ", logQueries=" + logQueries +
                ", bindingStrategy=" + bindingStrategy +
                '}';
    }
}
