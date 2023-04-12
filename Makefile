MAKE := $(shell which make)

clean:
	@if [ ! -d "./$(service)" ]; then \
  		echo "The service $(service) doesn't exist"; \
  		exit 1; \
	fi
	@$(MAKE) -C ./$(service) clean

test:
	@if [ ! -d "./$(service)" ]; then \
  		echo "The service $(service) doesn't exist"; \
  		exit 1; \
	fi
	@$(MAKE) -C ./$(service) test