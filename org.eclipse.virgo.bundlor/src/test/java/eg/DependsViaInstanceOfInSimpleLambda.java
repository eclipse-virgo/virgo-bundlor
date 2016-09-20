package eg;

import java.util.Arrays;
import java.util.List;

import deps.SomeClass;

public class DependsViaInstanceOfInSimpleLambda {
	
	List<Object> list = Arrays.asList(new Object[]{("uno"), ("dos"), ("tres")});
	
	public void foo() {
		
		list.forEach(o -> {			
			if(o instanceof SomeClass) {
				System.out.println(o);
			}
		});
	}
}
