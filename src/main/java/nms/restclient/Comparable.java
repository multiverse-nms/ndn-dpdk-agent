package nms.restclient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Comparable<T> {
	
	// Verify if a specific list contain a specific object based on ID 
	public  boolean contains(final List<T> list, final T element){
	    return list.stream().anyMatch(other -> other.equals(element));
	}
	
	// Get the union of two lists without Duplicates elements
	public  List <T> getUnionOfLists(List<T> list1, List<T> list2) {
		List<T> UnionFaces = new ArrayList<T>();
		UnionFaces.addAll(list1);
		list2.forEach(f -> {
			    if(!(contains(UnionFaces,f))) {
			    	UnionFaces.add(f);
			    }
		   });
		return UnionFaces;
			
		}
	// Get the difference between two lists 
	public   List<T> getDifferenceOfLists(List<T> list1, List<T> list2) {
		List<T> differentElements = list2.stream()
				.filter(element -> !list1.contains(element))
				.collect(Collectors.toList());
		
		if(!differentElements.isEmpty()) {
			return differentElements;
		}else {
			return Collections.emptyList();
		}
	}
}
	
