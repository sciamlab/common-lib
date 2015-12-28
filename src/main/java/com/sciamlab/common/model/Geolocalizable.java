package com.sciamlab.common.model;


public interface Geolocalizable {
	public String getAddress();
	public void setLocation(Double lat, Double lon);
}
