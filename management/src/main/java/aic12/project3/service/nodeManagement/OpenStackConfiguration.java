package aic12.project3.service.nodeManagement;

import org.jclouds.openstack.nova.v2_0.options.CreateServerOptions;

public class OpenStackConfiguration{
	
	private final String imageId = "91d5e341-f190-48ce-ae80-a578461fe652";	// Ubuntu 12.04 LTS amd64
	private final String flavorId = "1";	// m1.tiny
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
