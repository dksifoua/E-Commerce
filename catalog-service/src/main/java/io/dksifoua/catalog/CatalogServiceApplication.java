package io.dksifoua.catalog;

import io.dksifoua.catalog.entity.Category;
import io.dksifoua.catalog.repository.ICategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.data.repository.support.Repositories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

@SpringBootApplication
public class CatalogServiceApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(CatalogServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CatalogServiceApplication.class, args);
	}

	/*@EventListener
	public void handleContextRefresh(ContextRefreshedEvent event) {
		final Environment env = event.getApplicationContext().getEnvironment();

		LOGGER.info("Active profiles: {}", Arrays.toString(env.getActiveProfiles()));

		final MutablePropertySources sources = ((AbstractEnvironment) env).getPropertySources();

		StreamSupport.stream(sources.spliterator(), false)
				.filter(ps -> ps instanceof EnumerablePropertySource)
				.map(ps -> ((EnumerablePropertySource<?>) ps).getPropertyNames())
				.flatMap(Arrays::stream)
				.distinct()
				.filter(prop -> !(prop.contains("credentials") || prop.contains("password")))
				.forEach(prop -> LOGGER.info("{}: {}", prop, env.getProperty(prop)));
	}

	@EventListener
	public void populateDatabase(ApplicationReadyEvent event) {
		final ApplicationContext context = event.getApplicationContext();
		ICategoryRepository categoryRepository = (ICategoryRepository) new Repositories(context)
				.getRepositoryFor(Category.class)
				.orElseThrow(RuntimeException::new);
		List<Category> categories = new ArrayList<>();
		for(int i = 1; i <= 10; i++) {
			categories.add(
					Category
							.builder()
							.name(String.format("Category %02d", i))
							.description(String.format("Description %02d", i))
							.build()
			);
		}
		categoryRepository.saveAll(categories);
	}*/
}
