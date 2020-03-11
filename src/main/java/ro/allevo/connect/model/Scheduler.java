package ro.allevo.connect.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Scheduler {

	private boolean isInStandbyMode;

	public boolean isInStandbyMode() {
		return isInStandbyMode;
	}

	public void setInStandbyMode(boolean isInStandbyMode) {
		this.isInStandbyMode = isInStandbyMode;
	}
}
