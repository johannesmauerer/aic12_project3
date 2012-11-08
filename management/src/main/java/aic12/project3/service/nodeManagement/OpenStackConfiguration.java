package aic12.project3.service.nodeManagement;

import org.jclouds.openstack.nova.v2_0.options.CreateServerOptions;

public class OpenStackConfiguration{
	
	private final String imageId = "";
	private final String flavorId = "";
	private final CreateServerOptions [] createServerOptions;
	
	public OpenStackConfiguration(){
		createServerOptions = null;
	}
	
	public String getImageId() {
		return imageId;
	}

	public String getFlavorId() {
		return flavorId;
	}

	public CreateServerOptions[] getCreateServerOptions() {
		return createServerOptions;
	}	
}
