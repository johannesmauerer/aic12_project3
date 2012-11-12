package aic12.project3.service.nodeManagement;

public class Node{
	
	private String id;
	private String name;
	
	public Node(String name, String id){
		this.name = name;
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
