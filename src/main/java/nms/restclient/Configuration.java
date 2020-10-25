package nms.restclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class Configuration {
	private ArrayList<Face> faces = new ArrayList<>();
	private ArrayList<Route> routes = new ArrayList<>();

	private Map<Integer, Face> facesMap = new HashMap<>();
	private Map<Integer, Integer> faceIDsMapping = new HashMap<>();

	private static final Logger LOG = LoggerFactory.getLogger(Configuration.class);

	public Configuration() {
	}

	public Configuration(JsonObject json) {
		this.faces = new ArrayList<>();
		this.routes = new ArrayList<>();
		JsonArray facesArr = json.getJsonArray("faces");
		if (facesArr.size() > 0) {
			facesArr.forEach(jsonFace -> {
				if (jsonFace instanceof JsonObject) {
					Face face = new Face((JsonObject) jsonFace);
					this.faces.add(face);
					this.facesMap.put(face.getFwdId(), face);
				}
			});
		}
		JsonArray routesArr = json.getJsonArray("routes");
		if (routesArr.size() > 0) {
			routesArr.forEach(route -> {
				if (route instanceof JsonObject) {
					JsonObject routeObj = (JsonObject) route;
					LOG.debug(routeObj.toString());
					Route newRoute = new Route(routeObj);
//					newRoute.setFaceId(this.faceIDsMapping.get(newRoute.getFaceCtrlId()));
					this.routes.add(newRoute);
				}

			});
		}
	}

	public void addFace(Face face) {
		this.faces.add(face);
//		this.faces.forEach(r -> {
//			if(r.equals(face)) {
//				return;
//			}
//		});
//		this.faces.add(face);
//		this.facesMap.put(face.getFwdId(), face);
	}

	public void addRoute(Route route) {
		this.routes.forEach(r -> {
			if(r.equals(route)) {
				return;
			}
		});
		this.routes.add(route);
	}

	public void removeFace(int id) {
		this.faces.forEach(face -> {
			if(face.getCtrlId() == id) {
				this.faces.remove(face);
			}
		});
		this.facesMap.remove(id);
	}
	
	public void removeRoute(Route other) {
		this.routes.forEach(route -> {
			if(route.equals(other)) {
				this.routes.remove(route);
			}
		});
	}

	/**
	 * @return the faces
	 */
	public ArrayList<Face> getFaces() {
		return faces;
	}

	/**
	 * @param faces the faces to set
	 */
	public void setFaces(ArrayList<Face> faces) {
		this.faces = faces;
	}

	public Map<Integer, Face> getFacesMap() {
		return this.facesMap;
	}

	/**
	 * @return the routes
	 */
	public ArrayList<Route> getRoutes() {
		return routes;
	}

	/**
	 * @param routes the routes to set
	 */
	public void setRoutes(ArrayList<Route> routes) {
		this.routes = routes;
	}

	public Map<Integer, Integer> getFaceIDsMapping() {
		return faceIDsMapping;
	}
	
	public void addFaceIDsMapping(int ctrlId, int faceId) {
		this.faceIDsMapping.put(ctrlId, faceId);
	}

	public JsonObject toJsonObject() {
		JsonObject json = new JsonObject();
		JsonArray facesArr = new JsonArray();
		this.faces.forEach(face -> {
			facesArr.add(face.toJsonObject());
		});
		JsonArray routesArr = new JsonArray();
		this.routes.forEach(route -> {
			routesArr.add(route.toJsonObject());
		});
		json.put("faces", facesArr);
		json.put("routes", routesArr);
		return json;
	}

}
