package nms.restclient;

import java.util.ArrayList;
import java.util.List;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class Configuration {
	private List<Face> faces;
	private List<Route> routes;

	public Configuration() {
		this.faces = new ArrayList<>();
		this.routes = new ArrayList<>();
	}
	
	public Configuration(JsonObject json) {
		this.faces = new ArrayList<>();
		this.routes = new ArrayList<>();
		JsonArray facesArr = json.getJsonArray("faces");
		if (facesArr.size() > 0) {
			facesArr.forEach(face -> {
				if (face instanceof JsonObject)
					this.faces.add(new Face((JsonObject)face));
			});
		}
		JsonArray routesArr = json.getJsonArray("routes");
		if (routesArr.size() > 0) {
			routesArr.forEach(route -> {
				if (route instanceof JsonObject)
					this.routes.add(new Route((JsonObject)route));
			});
		}
	}

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

	public JsonObject toJsonObject() {
		JsonObject json = new JsonObject();
		JsonArray facesArr = new JsonArray();
		this.faces.forEach(face -> {
			facesArr.add(face);
		});
		JsonArray routesArr = new JsonArray();
		this.routes.forEach(route -> {
			routesArr.add(route);
		});
		json.put("faces", facesArr);
		json.put("routes", routesArr);
		return json;
	}


}
