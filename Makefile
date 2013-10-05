.PHONY: all deps compile tests test check clean

LEIN = $(shell which lein)

all:
	@$(LEIN) uberjar

deps:
	@$(LEIN) deps

compile: deps
	@$(LEIN) compile

tests test check: compile
	@$(LEIN) test

clean:
	@$(LEIN) clean
