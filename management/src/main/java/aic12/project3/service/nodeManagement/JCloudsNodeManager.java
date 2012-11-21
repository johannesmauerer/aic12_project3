package aic12.project3.service.nodeManagement;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jclouds.ContextBuilder;
import org.jclouds.collect.PagedIterable;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.NovaAsyncApi;
import org.jclouds.openstack.nova.v2_0.domain.Server;
import org.jclouds.openstack.nova.v2_0.domain.ServerCreated;
import org.jclouds.openstack.nova.v2_0.features.ServerApi;
import org.jclouds.openstack.v2_0.domain.Resource;
import org.jclouds.rest.RestContext;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

public class JCloudsNodeManager implements INodeManager{

	private static JCloudsNodeManager instance = new JCloudsNodeManager();
	private ComputeService compute;
	private RestContext<NovaApi, NovaAsyncApi> nova;
	private Set<String> zones;

	private JCloudsNodeManager(){}
	
	public static JCloudsNodeManager getInstance(){
		return instance;
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
	public Node startNode(String name) {

		if(name.isEmpty()){
			return null;
		}
		
		OpenStackConfiguration config = new OpenStackConfiguration();
		return startNode(name, config.getImageId(),config.getFlavorId());
	}
	
	@Override
	public Node startNode(String name, String image, String flavor) {
		
		if(name.isEmpty() || image.isEmpty() || flavor.isEmpty()){
			return null;
		}

		init();
		
		for (String zone: zones) {			

			ServerApi serverApi = nova.getApi().getServerApiForZone(zone);	
			ServerCreated created = serverApi.create(name, image, flavor, null);

			if(created != null){
				close();			
				return new Node(created.getName(), created.getId());
			}
		}

		close();
		return null;
	}

	@Override
	public boolean stopNode(String id) {

		if(id.isEmpty()){
			return false;
		}

		init();
		
		for (String zone: zones) {

			ServerApi serverApi = nova.getApi().getServerApiForZone(zone);
			boolean deleted = serverApi.delete(id);

			close();
			return deleted;
		}

		close();
		return false;
	}

	@Override
	public List<Node> listNodes() {

		List<Node> nodeList = new ArrayList<Node>();
		
		init();
		
		for (String zone: zones) {		

			ServerApi serverApi = nova.getApi().getServerApiForZone(zone);

			FluentIterable<? extends Server> list = serverApi.listInDetail().concat();	
			
			for(Server server: list){
				nodeList.add(new Node(server.getName(),server.getId()));
			}				
		}

		close();
		return nodeList;
	}
}
