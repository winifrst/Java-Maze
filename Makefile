MVN ?= mvn

.PHONY: all build test package install clean

all: build

build:
	$(MVN) -q compile

test:
	$(MVN) test

package:
	$(MVN) -q package

install:
	$(MVN) -q install

clean:
	$(MVN) clean

dvi:
	@echo "No documentation to build"

dist:
	$(MVN) -q package