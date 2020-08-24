package nms.restclient;

import java.util.List;

public class Configuration {
	private List<Face> faces;
	private List<Route> routes;
	
	
	
	public void addFace(Face face) {
		this.faces.add(face);
	}
	
	public void addRoute(Route route) {
		this.routes.add(route);
	}
	
	/**
	 * @return the faces
	 */
	public List<Face> getFaces() {
		return faces;
	}
	/**
	 * @param faces the faces to set
	 */
	public void setFaces(List<Face> faces) {
		this.faces = faces;
	}
	/**
	 * @return the routes
	 */
	public List<Route> getRoutes() {
		return routes;
	}
	/**
	 * @param routes the routes to set
	 */
	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}
	
	
	

}
