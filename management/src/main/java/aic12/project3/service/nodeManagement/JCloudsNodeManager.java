package aic12.project3.service.nodeManagement;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.List;
import java.util.Set;

import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.NovaAsyncApi;
import org.jclouds.openstack.nova.v2_0.domain.Image;
import org.jclouds.openstack.nova.v2_0.features.ImageApi;
import org.jclouds.openstack.nova.v2_0.features.ServerApi;
import org.jclouds.rest.RestContext;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

public class JCloudsNodeManager implements INodeManager{

	private ComputeService compute;
	private RestContext<NovaApi, NovaAsyncApi> nova;
	private Set<String> zones;
	
	public JCloudsNodeManager(){
	}
	
	private void init(){
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void uncaughtException(Thread t, Throwable e) {
				if (compute != null) close();
				e.printStackTrace();
				System.exit(1);
			}
		});

		Iterable<Module> modules = ImmutableSet.<Module> of();

		String provider = "openstack-nova";
		String identity = "aic12w01:aic12w01"; // tenantName:userName
		String password = "Ofein3Ku";

		ComputeServiceContext context = ContextBuilder.newBuilder(provider)
				.credentials(identity, password)
				.endpoint("http://openstack.infosys.tuwien.ac.at:5000/v2.0")
				.modules(modules)
				.buildView(ComputeServiceContext.class);
		compute = context.getComputeService();
		nova = context.unwrap();
		zones = nova.getApi().getConfiguredZones();
	}
	
	private void close() {
		compute.getContext().close();
	}
	
	@Override
	public boolean startNode(String name) {
		
		init();
		
		if(name.isEmpty()){
			close();
			return false;
		}
		
		for (String zone: zones) {
			
			System.out.println("Zone: " + zone);
			
			ServerApi serverApi = nova.getApi().getServerApiForZone(zone);
			
			OpenStackConfiguration config = new OpenStackConfiguration();		
			serverApi.create(name, config.getImageId(), config.getFlavorId(),config.getCreateServerOptions());
			
			// TBD: Check if started
			close();
			return true;
		}
		
		close();
		return false;
	}

	@Override
	public boolean startNode(String name, int flavor, String image) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean stopNode(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<INode> listNodes() {
		// TODO Auto-generated method stub
		return null;
	}

}
