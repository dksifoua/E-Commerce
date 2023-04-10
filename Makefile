CATALOG_SERVICE_DIR := ./catalog-service

GRADLE := $(shell which gradle)

.ONESHELL:
run:
	cd $(CATALOG_SERVICE_DIR)
	$(GRADLE) bootRun