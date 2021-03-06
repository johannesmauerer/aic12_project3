package aic12.project3.service.nodeManagement;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.NovaAsyncApi;
import org.jclouds.openstack.nova.v2_0.domain.Address;
import org.jclouds.openstack.nova.v2_0.domain.Server;
import org.jclouds.openstack.nova.v2_0.domain.ServerCreated;
import org.jclouds.openstack.nova.v2_0.features.ServerApi;
import org.jclouds.rest.RestContext;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.inject.Module;

public class JCloudsNodeManager implements ILowLevelNodeManager{

	private static JCloudsNodeManager instance = new JCloudsNodeManager();
	private ComputeService compute;
	private RestContext<NovaApi, NovaAsyncApi> nova;
	private Set<String> zones;
	private Logger logger = Logger.getRootLogger();

	private JCloudsNodeManager(){}

	public static JCloudsNodeManager getInstance(){
		return instance;
	}

	protected void initOnce(){

		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void uncaughtException(Thread t, Throwable e) {
				e.printStackTrace();
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

	private void init(){
	}

	private void close() {
		//compute.getContext().close();
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

		try {
			for (String zone: zones) {			

				ServerApi serverApi = nova.getApi().getServerApiForZone(zone);	
				ServerCreated created = serverApi.create(name, image, flavor);

				if(created != null){

					close();
					Node n = new Node(created.getName(), created.getId());

					// TODO: Please change to actually make it work :)
					n.setIp(this.getIp(created.getId()));
					return n;
				}
			}

			close();
		} catch (org.jclouds.rest.InsufficientResourcesException e) {
			logger.warn("not enough resources from JClouds");
		}
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
	public List<Node> listRunningNodes() {

		List<Node> nodeList = new ArrayList<Node>();

		init();

		for (String zone: zones) {		

			ServerApi serverApi = nova.getApi().getServerApiForZone(zone);

			FluentIterable<? extends Server> list = serverApi.listInDetail().concat();	

			for(Server server: list){



				Node n = new Node(server.getName(),server.getId());

				Multimap<String, Address> map = server.getAddresses();
				Set keySet = map.keySet( );  
				Iterator keyIterator = keySet.iterator();  

				// Get Address (only one available, if not take last one)
				String address = "";
				while( keyIterator.hasNext( ) ) {  
					Object key = keyIterator.next( );  
					Collection values = (Collection) map.get( (String) key );  
					Iterator valuesIterator = values.iterator( );  
					while( valuesIterator.hasNext( ) ) {  
						Address ad = (Address) valuesIterator.next();
						address = ad.getAddr();  
					}    
				}  

				n.setIp(address);

				nodeList.add(n);
			}				
		}

		close();
		return nodeList;
	}


	@Override
	public String getIp(final String id) {
		init();

		for (String zone: zones) {             

			ServerApi serverApi = nova.getApi().getServerApiForZone(zone);

			FluentIterable<? extends Server> list = serverApi.listInDetail().concat();     

			Predicate<Server> matchesServerIdPredicate = new Predicate<Server>() {
				@Override
				public boolean apply(Server server) {
					return (server.getId().equals(id));
				}              
			};

			for(Server server: list.filter(matchesServerIdPredicate)){
				Multimap<String, Address> map = server.getAddresses();
				Iterator<String> keyIterator = map.keySet().iterator();  

				// Get Address (only one available, if not take last one)
				String address = "";
				while( keyIterator.hasNext( ) ) {  
					Collection<Address> values = map.get( (String) keyIterator.next( ) );  
					Iterator<Address> valuesIterator = values.iterator( );  
					while(valuesIterator.hasNext()) {
						Address ad = (Address) valuesIterator.next();
						address = ad.getAddr();  
					}    
				}  
				close();
				return address;
			}                              
		}

		close();
		return null;
	}




}
