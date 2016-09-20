package eg;

import deps.ClassWithStatics;

public interface DependsViaStaticMethodCallInDefaultImplementation {

	default void methodWithDepend() {
		ClassWithStatics.doSomething();
	}
}
