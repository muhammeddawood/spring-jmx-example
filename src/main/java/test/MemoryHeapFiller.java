package test;


import java.util.ArrayList;
import java.util.Collection;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MemoryHeapFiller {

	private static final Collection<Double> GARBAGE = new ArrayList<Double>();

	@Scheduled(fixedDelay = 2000)
	public void addGarbage() {
		System.out.println("Adding data...");
		for (int i = 0; i < 1000000; i++) {
			GARBAGE.add(Math.random());
		}
	}

}

