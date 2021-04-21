package nms.forwarder.model.face;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FaceStoreRecord {

	private List<Integer> faces;
	private boolean faceScope;

	public FaceStoreRecord() {
		this.faces = new ArrayList<>();
		this.faceScope = false;
	}

	public void setFaces(ArrayList<Integer> faces) {
		Collections.copy(this.faces, faces);
	}

	public void setFaceScope(boolean scope) {
		this.faceScope = scope;
	}

	public void addFace(int faceId) {
		this.faces.add(faceId);
	}
	
	public void removeFace(int faceId) {
		this.faces.remove(faceId);
	}

	public List<Integer> getFaces() {
		return this.faces;
	}

	public boolean getScope() {
		return this.faceScope;
	}

	public static void main(String[] args) {

		Map<String, FaceStoreRecord> storedFaces = new HashMap<>();
		storedFaces.put("1", new FaceStoreRecord());
		FaceStoreRecord record = storedFaces.get("1");
		record.addFace(12);
		FaceStoreRecord record2 = storedFaces.get("2");
		record2 = new FaceStoreRecord();
		record2.addFace(22);
		System.out.println("size=" + storedFaces.get("1").getFaces().size());
		System.out.println("size=" + storedFaces.get("2").getFaces().size());

	}
}
